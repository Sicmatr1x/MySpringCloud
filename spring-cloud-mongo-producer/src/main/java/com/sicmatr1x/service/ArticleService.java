package com.sicmatr1x.service;

import com.sicmatr1x.pojo.Article;

import java.util.List;

public interface ArticleService {
    public void saveArticle(Article article);
    public Article findOneArticleByURL(String url);
    public List<Article> findRecentlyArticles(Integer number);
    public List<Article> searchArticlesByTitle(String keyword, Integer pageBegin, Integer pageSize);
}
