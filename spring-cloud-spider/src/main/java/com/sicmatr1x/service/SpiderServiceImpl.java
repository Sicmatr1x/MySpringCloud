package com.sicmatr1x.service;

import com.sicmatr1x.pojo.Article;
import com.sicmatr1x.pojo.ArticleSource;
import com.sicmatr1x.spider.func.GeneralHtmlParser;
import com.sicmatr1x.spider.func.HttpUtil;
import com.sicmatr1x.spider.func.NoteParser;
import com.sicmatr1x.spider.func.ZhiHuParser;
import com.sicmatr1x.spider.translator.Translator;
import com.sicmatr1x.spider.translator.ZhihuImgTranslator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sicmatr1x.spider.HuxiuHtmlUtil;
import com.sicmatr1x.spider.ZhihuHtmlUtil;
import com.sicmatr1x.spider.ZhihuZhuanlanHtmlUtil;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class SpiderServiceImpl implements SpiderService {

    @Autowired
    ArticleService articleService;

    @Autowired
    private ZhihuHtmlUtil zhihuHtmlUtil;

    @Autowired
    private ZhihuZhuanlanHtmlUtil zhihuZhuanlanHtmlUtil;

    @Autowired
    private HuxiuHtmlUtil huxiuHtmlUtil;

    private static final Logger logger = Logger.getLogger(SpiderServiceImpl.class);

    private final Function<String, String> loggerError = (errMsg) -> {
        logger.error(errMsg);
        return errMsg;
    };

    /**
     * 截取 article 对象的body字段，避免响应体过大
     * @param article
     */
    private void smaller(Article article) {
        if (article.getBody() != null) {
            article.setBody(article.getBody().substring(0, 400) + "......");
        }
    }


    @Override
    public Article spiderURL(Article article) throws IOException {
        // 使用URL查询数据库，若已存在则不爬取
        Article articleResult = articleService.findOneArticleByURL(article.getUrl());
        if (articleResult != null) {
            logger.warn("Article is exist: url=" + article.getUrl());
            smaller(articleResult);
            return articleResult;
        }
        String url = article.getUrl();
        logger.info("url=" + url);

        // 开始爬取
//        if (url.contains(domain)) {
//
//        }
        return null;

    }

    @Override
    public Article spiderZhihuAnswer(Article article) throws IOException {
        final String DOMAIN = "https://www.zhihu.com/";
        // 使用URL查询数据库，若已存在则不爬取
        Article articleResult = articleService.findOneArticleByURL(article.getUrl());
        if (articleResult != null) {
            logger.warn("Article is exist: url=" + article.getUrl());
            smaller(articleResult);
            return articleResult;
        }
        // 开始爬取
        String url = article.getUrl();
        logger.info("url=" + url);

        // get html
        Document originDoc = HttpUtil.get(url);
        // parse title
        String title = GeneralHtmlParser.parseTitle.getText(originDoc);
        if (StringUtils.isBlank(title)) {
            article.setTitle(loggerError.apply("Analyze Page Error: Could not parse title, url=" + url));
            return article;
        }
        article.setTitle(title);
        // begin to parse body
        // 判断是知乎文章还是回答
        String mode = url.substring(DOMAIN.length(), url.indexOf("/", DOMAIN.length()));
        Map<String, String> noteParams = new HashMap<>();
        noteParams.put("title", title);
        noteParams.put("url", url);
        String content = null;
        if (StringUtils.equals("question", mode)) {
            if (url.contains("answer")) {
                // 单个回答链接
                Element answerElement = ZhiHuParser.answer.get(originDoc);
                answerElement = ZhiHuParser.img.parse(answerElement);
                // 生成笔记html
                content = NoteParser.note.generate(answerElement, noteParams);
            } else {
                // 问题链接
                // TODO:
            }
        } else if (StringUtils.equals("p", mode)) {
            // 文章链接
            Element answerElement = ZhiHuParser.zhuanLan.get(originDoc);
            answerElement = ZhiHuParser.img.parse(answerElement);
            // 生成笔记html
            content = NoteParser.note.generate(answerElement, noteParams);
        }

        // save to db
        article.setBody(content);
        article.setCreatedTime(new Date());
        if (StringUtils.isBlank(article.getBody())) {
            logger.warn("Body is Blank: url=" + url);
            return article;
        }
        articleService.saveArticle(article);

        smaller(article);
        return article;
    }

    @Override
    public Article spiderZhihuZhuanLan(Article article) throws IOException {
        // 使用URL查询数据库，若已存在则不爬取
        Article articleResult = articleService.findOneArticleByURL(article.getUrl());
        if (articleResult != null) {
            smaller(articleResult);
            return articleResult;
        }
        // 开始爬取
        zhihuZhuanlanHtmlUtil.setAddress(article.getUrl());
        zhihuZhuanlanHtmlUtil.parse();
        article.setTitle(zhihuZhuanlanHtmlUtil.getTitle());
        article.setBody(zhihuZhuanlanHtmlUtil.getContent());
        article.setCreatedTime(new Date());
        articleService.saveArticle(article);

        smaller(article);
        return article;
    }

    @Override
    public Article spiderHuxiu(Article article) throws IOException {
        // 使用URL查询数据库，若已存在则不爬取
        Article articleResult = articleService.findOneArticleByURL(article.getUrl());
        if (articleResult != null) {
            smaller(articleResult);
            return articleResult;
        }
        // 开始爬取
        huxiuHtmlUtil.setAddress(article.getUrl());
        huxiuHtmlUtil.parse();
        article.setTitle(huxiuHtmlUtil.getTitle());
        article.setBody(huxiuHtmlUtil.getContent());
        article.setCreatedTime(new Date());
        articleService.saveArticle(article);

        smaller(article);
        return article;
    }
}
