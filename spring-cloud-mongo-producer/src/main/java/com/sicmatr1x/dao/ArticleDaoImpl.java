package com.sicmatr1x.dao;

import com.sicmatr1x.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ArticleDaoImpl implements ArticleDao{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void saveArticle(Article article) {
        mongoTemplate.save(article);
    }

    @Override
    public Article findOneArticleByURL(String url) {
        Query query = new Query(Criteria.where("url").is(url));
        Article articleEntity = mongoTemplate.findOne(query, Article.class);
        return articleEntity;
    }

    @Override
    public List<Article> findRecentlyArticles(Integer number) {
        Sort sort = new Sort(Sort.Direction.DESC, "createdTime");
        Query query = new Query();
        query.with(sort);
        query.fields().exclude("body");
        query.limit(number != null && number > 0 ? number : 3);
        List<Article> result = mongoTemplate.find(query, Article.class);
        return result;
    }
}
