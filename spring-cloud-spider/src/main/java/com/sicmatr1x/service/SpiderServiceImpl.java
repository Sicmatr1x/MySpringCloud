package com.sicmatr1x.service;

import com.sicmatr1x.pojo.Article;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sicmatr1x.spider.HuxiuHtmlUtil;
import com.sicmatr1x.spider.ZhihuHtmlUtil;
import com.sicmatr1x.spider.ZhihuZhuanlanHtmlUtil;

import java.io.IOException;
import java.util.Date;

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
    public Article spiderZhihuAnswer(Article article) throws IOException {
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
        zhihuHtmlUtil.setAddress(url);
        zhihuHtmlUtil.parse();
        article.setTitle(zhihuHtmlUtil.getTitle());
        article.setBody(zhihuHtmlUtil.getContent());
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
