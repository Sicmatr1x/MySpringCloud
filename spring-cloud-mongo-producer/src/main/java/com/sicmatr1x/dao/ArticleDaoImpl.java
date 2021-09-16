package com.sicmatr1x.dao;

import com.sicmatr1x.pojo.Article;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.regex.Pattern;

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

    @Override
    public List<Article> searchArticlesByTitle(String keyword, Integer pageBegin, Integer pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "createdTime");
        Query query = new Query();
        Pattern pattern = Pattern.compile(".*?" + escapeSpecialWord(keyword) + ".*");
        Criteria c5 = Criteria.where("title").regex(pattern);
        query.addCriteria(c5);
        query.with(sort);
        if (pageSize > 0) {
            Pageable pageable = new PageRequest(pageBegin, pageSize);
            query.with(pageable);
        }
        query.fields().exclude("body");
        List<Article> result = mongoTemplate.find(query, Article.class);
        return result;
    }

    @Override
    public List<Article> searchArticlesByBody(String keyword, Integer pageBegin, Integer pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "createdTime");
        Query query = new Query();
        Pattern pattern = Pattern.compile(".*?" + escapeSpecialWord(keyword) + ".*");
        Criteria c5 = Criteria.where("body").regex(pattern);
        query.addCriteria(c5);
        query.with(sort);
        if (pageSize > 0) {
            Pageable pageable = new PageRequest(pageBegin, pageSize);
            query.with(pageable);
        }
        query.fields().exclude("body");
        List<Article> result = mongoTemplate.find(query, Article.class);
        return result;
    }

    @Override
    public Article deleteOneArticleByURL(String url) {
        Query query = new Query(Criteria.where("url").is(url));
        Article articleEntity = mongoTemplate.findOne(query, Article.class);
        mongoTemplate.remove(articleEntity);
        articleEntity.setBody("");
        return articleEntity;
    }

    private String escapeSpecialWord(String keyword) {
        if (StringUtils.isNotBlank(keyword)) {
            String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }
}
