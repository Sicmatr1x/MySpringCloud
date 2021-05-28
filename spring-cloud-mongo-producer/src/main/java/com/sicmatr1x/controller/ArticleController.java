package com.sicmatr1x.controller;

import com.sicmatr1x.pojo.Article;
import com.sicmatr1x.service.ArticleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @RequestMapping(value = "/article/save", method = RequestMethod.POST)
    public boolean saveArticle(@RequestBody Article article) throws IOException {
        articleService.saveArticle(article);
        return true;
    }

    @RequestMapping(value = "/article", method = RequestMethod.GET)
    public Article findOneArticleById(@RequestParam String url) throws IOException {
        Article article = null;
        String[] work = url.split("\\?");
        article = articleService.findOneArticleByURL(work[0]);
        return article;
    }

    @RequestMapping(value = "/recently/articles", method = RequestMethod.GET)
    public List<Article> findRecentlyArticles(@RequestParam(required = false) Integer number) throws IOException {
        List<Article> list = articleService.findRecentlyArticles(number);
        return list;
    }

    @RequestMapping(value = "/articles/search", method = RequestMethod.GET)
    public List<Article> searchArticles(@RequestParam(required = true) String keyword,
                                        @RequestParam(required = true) String type,
                                        @RequestParam(required = false) Integer pageBegin,
                                        @RequestParam(required = false) Integer pageSize) throws IOException {
        if (StringUtils.equals("title", type)) {
            // {"title": {$regex:/es/}}
            //TODO
            List<Article> list = articleService.searchArticlesByTitle(keyword, pageBegin, pageSize);
            return list;
        } else if (StringUtils.equals("body", type)) {
            List<Article> list = articleService.searchArticlesByBody(keyword, pageBegin, pageSize);
            return list;
        } else {
            //TODO
        }
        return null;
    }
}
