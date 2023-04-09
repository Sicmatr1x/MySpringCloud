package com.sicmatr1x.spider;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import com.sicmatr1x.spider.translator.*;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ZhihuHtmlUtil extends HtmlUtil {

    public static final String DOMAIN = "https://www.zhihu.com/";

    private static final Logger logger = Logger.getLogger(ZhihuHtmlUtil.class);

    @Override
    void getHtml() throws IOException {
        Map<String, String> cookies = new HashMap<>();
//    cookies.put("MQTT::auth", "{\"username\":\"zhihu_web/c3eba22b84626619308c435efce4eba0\",\"password\":\"1551688934/9c50faaa4884b954a9f5020c10bef1535edc930b\"}");
//    cookies.put("lastuser", "\"2499055595@qq.com\"");
//    cookies.put("lastuser:email", "2499055595@qq.com");
//    cookies.put("privacy-records", "{\"undefined\":1548381571678}");
        cookies.put("zap:storage:local", "{\"canUseDataViewPayload\":true,\"lastUA\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36\"}");
        this.doc = Jsoup.connect(this.address).cookies(cookies).get();
    }

    /**
     * 获取img dom
     *
     * @param element 包含回答内容的DOM
     * @return
     */
    public Element translateImgDom(Element element) {
        // 移除之后回答DOM下多余的noscript DOM
        Elements noscriptNode = element.select("noscript");
        if (noscriptNode.size() == 0) {
            return element;
        }
        noscriptNode.first().remove();
        // 获取回答里面的img DOM
        Elements imgElements = element.select("img");
        // 遍历img DOM
        Translator zhihuImgTranslator = new ZhihuImgTranslator();
        Translator img2Base64Translator = new Img2Base64Translator();
        for (Element imgElement : imgElements) {
            // 转换img DOM的src从互联网URL为本地URL
            imgElement = zhihuImgTranslator.translate(imgElement);
            // 下载img
            this.downloadImg(imgElement);
        }
        return element;
    }

    /**
     * 下载img DOM中的图片到本地 文章title + _files文件夹下
     *
     * @param element img DOM元素
     * @return
     */
    private Element downloadImg(Element element) {
        ImgDownloader imgDownloader = new ImgDownloader();
        return imgDownloader.translate(element, this.title + "_files");
    }

    /**
     * 从回答页面的HTML中解析第一个主回答内容
     *
     * @return 回答内容
     */
    public Element getOneAnswer() {
        Elements questionMainColumn = this.doc.select(".Question-mainColumn");
        if (CollectionUtils.isEmpty(questionMainColumn)) {
            System.out.println("error: can not found class=\"Question-mainColumn\" in " + this.address);
        } else {
            Elements cards = questionMainColumn.get(0).select(".AnswerCard");
            Elements answerItems = cards.get(0).select(".AnswerItem");
            Elements richContent = answerItems.get(0).select(".RichContent");
            Elements richText = richContent.get(0).select(".RichText");
            return richText.get(0);
        }
        return null;
    }

    public String getQuestionTitle() {
        String title;
        try {
            Elements question = this.doc.select(".QuestionHeader-title");
            if (CollectionUtils.isEmpty(question)) {
                logger.warn("Analyze Page Warn:" + this.address + " could not select(.QuestionHeader-title)");
                Elements titleNode = this.doc.select("title");
                System.out.println(titleNode.text());
                return titleNode.text();
            }
            title = question.get(0).text();
        } catch (IndexOutOfBoundsException error) {
            logger.error("Analyze Page Error:" + this.address, error);
            return null;
        }
        return title;
    }

    public void getAnswerList() {
        // TODO
    }

    public void getArticle() {
        // TODO
    }

    @Override
    boolean initAddress() {
        if (this.address == null || this.address.length() < DOMAIN.length()) {
            System.out.print("Address is too short:" + this.address);
            return false;
        }
        return true;
    }

    @Override
    void analysisPage() {
        HeadTitleTranslator headTitleTranslator = new HeadTitleTranslator();
        headTitleTranslator.setFromAddress(this.getAddress());

        int endIndex = this.address.indexOf("/", DOMAIN.length());
        String mode = this.address.substring(DOMAIN.length(), endIndex);
        this.title = this.getQuestionTitle();
        if (StringUtils.isBlank(this.title)) {
            return;
        } else if (StringUtils.equals("知乎 - 有问题，上知乎", this.title)) {
            logger.warn("Skip title:知乎 - 有问题，上知乎 url=" + this.address);
            return;
        } else if (StringUtils.equals("你似乎来到了没有知识存在的荒原 - 知乎", this.title)) {
            logger.warn("Skip title:你似乎来到了没有知识存在的荒原 - 知乎 url=" + this.address);
            return;
        }
        headTitleTranslator.setTitleText(this.title);

        if (StringUtils.equals("question", mode)) {
          if (this.address.contains("answer")) {
            Element answerElement = this.getOneAnswer(); // 单个回答链接
            Translator zhihuImgTranslator = new ZhihuImgTranslator();
            answerElement = zhihuImgTranslator.translate(answerElement);
            this.content = headTitleTranslator.translate(answerElement).html();
            } else {
                this.getAnswerList(); // 问题链接
            }
        } else if (StringUtils.equals("p", mode)) { // 文章链接
            this.getArticle();
        }
    }

}
