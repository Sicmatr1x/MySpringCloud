package com.sicmatr1x.spider.func;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;

/**
 * @author sicmatr1x@outlook.com
 * @date 2023-04-14 16:04
 */
public class ZhiHuParser {

    private static final Logger logger = Logger.getLogger(ZhiHuParser.class);

    /**
     * find answer dom in html
     */
    public static final Document2Element answer = (doc) -> {
        Elements questionMainColumn = doc.select(".Question-mainColumn");
        if (CollectionUtils.isEmpty(questionMainColumn)) {
            System.out.println("error: can not found class=\"Question-mainColumn\" in " + doc.baseUri());
        } else {
            Elements cards = questionMainColumn.get(0).select(".AnswerCard");
            Elements answerItems = cards.get(0).select(".AnswerItem");
            Elements richContent = answerItems.get(0).select(".RichContent");
            Elements richText = richContent.get(0).select(".RichText");
            return richText.get(0);
        }
        return null;
    };

    /**
     * find answer dom in html
     */
    public static final Document2Element zhuanLan = (doc) -> {
        Elements articleRichText = doc.select(".Post-RichText");
        if (articleRichText.isEmpty()) {
            System.out.println("error: can not found class=\"Post-RichText\" in " + doc.baseUri());
        } else {
            return articleRichText.first();
        }
        return null;
    };

    /**
     * 转换img DOM的src从互联网URL为本地URL
     */
    private static final ElementParseFunc imgDomAttr = (imgElement) -> {
        String srcAddress = null;
        // smart get img src url
        Attributes attrs = imgElement.attributes();
        Iterator<Attribute> iterator = attrs.iterator();
        while (iterator.hasNext()) {
            Attribute attr = iterator.next();
            if (attr.getValue() != null && attr.getValue().contains("http")) {
                srcAddress = attr.getValue();
            }
        }

        String rawWidth = imgElement.attr("data-rawwidth");
        String rawHeight = imgElement.attr("data-rawheight");
        imgElement.attr("src", srcAddress);
        imgElement.attr("style", "width:" + rawWidth + ";height:" + rawHeight);
        imgElement.removeAttr("data-caption");
        imgElement.removeAttr("data-size");
        imgElement.removeAttr("data-rawwidth");
        imgElement.removeAttr("data-rawheight");
        imgElement.removeAttr("data-default-watermark-src");
        imgElement.removeAttr("data-original");
        imgElement.removeAttr("data-actualsrc");
        return imgElement;
    };

    /**
     * parse img in dom
     */
    public static final ElementParseFunc img = (element) -> {
        // 移除之后回答DOM下多余的noscript DOM
        Elements noScriptNode = element.select("noscript");
        if (noScriptNode == null || noScriptNode.size() == 0) {
            logger.warn("Could not get select(noscript):noScriptNode=" + noScriptNode);
            return element;
        }
        noScriptNode.first().remove();
        // 获取回答里面的img DOM
        Elements imgElements = element.select("img");
        imgElements.forEach((imgElement) -> {
            // 转换img DOM的src从互联网URL为本地URL
            Element e = imgDomAttr.parse(imgElement);
            // img转换成base64
            if (e != null) {
                Element base64Ele = ImgParser.imgDomBase64.parse(e);
                int size = base64Ele.parentNode().outerHtml().length();
                logger.info(" => " + size + " byte, " + (size*1.0 / 1024)/1024 + " mb");
            }

        });
        return element;
    };



}
