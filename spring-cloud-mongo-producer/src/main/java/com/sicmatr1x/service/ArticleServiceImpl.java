package com.sicmatr1x.service;

import com.sicmatr1x.dao.ArticleDao;
import com.sicmatr1x.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService{

    @Autowired
    private ArticleDao articleDao;

    @Override
    public void saveArticle(Article article) {
        articleDao.saveArticle(article);
    }

    @Override
    public Article findOneArticleByURL(String url) {
        return articleDao.findOneArticleByURL(url);
    }

    @Override
    public List<Article> findRecentlyArticles(Integer number) {
        return articleDao.findRecentlyArticles(number);
    }

    @Override
    public List<Article> searchArticlesByTitle(String keyword, Integer pageBegin, Integer pageSize) {
        return articleDao.searchArticlesByTitle(keyword, pageBegin, pageSize);
    }

    @Override
    public List<Article> searchArticlesByBody(String keyword, Integer pageBegin, Integer pageSize) {
        return articleDao.searchArticlesByBody(keyword, pageBegin, pageSize);
    }

    @Override
    public Article deleteOneArticleByURL(String url) {
        return articleDao.deleteOneArticleByURL(url);
    }
}
