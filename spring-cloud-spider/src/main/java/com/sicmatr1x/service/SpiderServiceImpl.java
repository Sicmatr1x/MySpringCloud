package com.sicmatr1x.service;

import com.sicmatr1x.pojo.Article;
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

    @Override
    public Article spiderZhihuAnswer(Article article) throws IOException {
        // 使用URL查询数据库，若已存在则不爬取
        Article articleResult = articleService.findOneArticleByURL(article.getUrl());
        if (articleResult != null) {
            return articleResult;
        }
        // 开始爬取
        zhihuHtmlUtil.setAddress(article.getUrl());
        zhihuHtmlUtil.parse();
        article.setTitle(zhihuHtmlUtil.getTitle());
        article.setBody(zhihuHtmlUtil.getContent());
        article.setCreatedTime(new Date());
        articleService.saveArticle(article);

        return article;
    }

    @Override
    public Article spiderZhihuZhuanLan(Article article) throws IOException {
        // 使用URL查询数据库，若已存在则不爬取
        Article articleResult = articleService.findOneArticleByURL(article.getUrl());
        if (articleResult != null) {
            return articleResult;
        }
        // 开始爬取
        zhihuZhuanlanHtmlUtil.setAddress(article.getUrl());
        zhihuZhuanlanHtmlUtil.parse();
        article.setTitle(zhihuZhuanlanHtmlUtil.getTitle());
        article.setBody(zhihuZhuanlanHtmlUtil.getContent());
        article.setCreatedTime(new Date());
        articleService.saveArticle(article);
        return article;
    }

    @Override
    public Article spiderHuxiu(Article article) throws IOException {
        // 使用URL查询数据库，若已存在则不爬取
        Article articleResult = articleService.findOneArticleByURL(article.getUrl());
        if (articleResult != null) {
            return articleResult;
        }
        // 开始爬取
        huxiuHtmlUtil.setAddress(article.getUrl());
        huxiuHtmlUtil.parse();
        article.setTitle(huxiuHtmlUtil.getTitle());
        article.setBody(huxiuHtmlUtil.getContent());
        article.setCreatedTime(new Date());
        articleService.saveArticle(article);
        return article;
    }
}
