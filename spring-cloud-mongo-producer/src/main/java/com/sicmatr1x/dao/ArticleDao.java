package com.sicmatr1x.dao;

import com.sicmatr1x.pojo.Article;

import java.util.List;

public interface ArticleDao {

    public void saveArticle(Article article);

    public Article findOneArticleByURL(String url);

    public List<Article> findRecentlyArticles(Integer number);

    public List<Article> searchArticlesByTitle(String keyword, Integer pageBegin, Integer pageSize);

    public List<Article> searchArticlesByBody(String keyword, Integer pageBegin, Integer pageSize);
}
