package com.sicmatr1x.dao;

import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.sicmatr1x.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SyncArticleDaoImpl implements SyncArticleDao{

    @Autowired
    private MongoTemplate mongoTargetTemplate;

    private MongoTemplate mongoSourceTemplate;

    MongoTemplate createTemplate(Map<String, String> mongodbMap) {
        String username = "NoteBookServer_appln"; // spring.data.mongodb.username
        String password = "Friday5"; // spring.data.mongodb.password
        String host = "127.0.0.1"; // spring.data.mongodb.host
        int port = 27017; // spring.data.mongodb.port
        String database = "NoteBookServer"; // spring.data.mongodb.database
        if (mongodbMap != null) {
            host = mongodbMap.get("host");
            port = Integer.parseInt(mongodbMap.get("port"));
            database = mongodbMap.get("database");
            username = mongodbMap.get("username");
            password = mongodbMap.get("password");
        }

        List<MongoCredential> credentialsList = new ArrayList<>();
        credentialsList.add(MongoCredential.createCredential(username, database, password.toCharArray()));
        MongoClient mongoClient = new MongoClient(new ServerAddress(host, port), credentialsList);
        MongoDbFactory factory = new SimpleMongoDbFactory(mongoClient, database);

        return new MongoTemplate(factory);
    }


    @Override
    public String testConnectionToSourceDB(Map<String, String> mongodbMap) {
        this.mongoSourceTemplate = createTemplate(mongodbMap);
        DBCollection dbCollection = this.mongoSourceTemplate.getCollection("NoteBookServer");

        if (dbCollection == null) {
            return null;
        } else {
//        Long count = dbCollection.getCount();
            return dbCollection.toString();
        }
    }

    @Override
    public List<Article> readArticlesFromSourceDB() {
        Sort sort = new Sort(Sort.Direction.DESC, "createdTime");
        Query query = new Query();
        query.with(sort);
        query.fields().exclude("body");
        List<Article> result = this.mongoSourceTemplate.find(query, Article.class);
        return result;
    }

    @Override
    public List<Article> readArticlesFromTargetDB() {
        Sort sort = new Sort(Sort.Direction.DESC, "createdTime");
        Query query = new Query();
        query.with(sort);
        query.fields().exclude("body");
        List<Article> result = this.mongoTargetTemplate.find(query, Article.class);
        return result;
    }

    @Override
    public Integer margeSourceArticlesToTargetDB(boolean additionalMode, List<Article> source, List<Article> target) {
        int affectCount = 0;
        if (additionalMode) { // true为追加模式
            List<Article> missingInTarget = new ArrayList<>();
            Map<String, Integer> targetMap = new HashMap<>();
            for (int i = 0; i < target.size(); i++) {
                targetMap.put(target.get(i).getUrl(), i);
            }
            for (int i = 0; i < source.size(); i++) {
                Article cur = source.get(i);
                if (!targetMap.containsKey(cur.getUrl())) {
                    missingInTarget.add(cur);
                }
            }
            this.mongoTargetTemplate.insert(missingInTarget, Article.class);
            affectCount = missingInTarget.size();
        } else {
            this.mongoTargetTemplate.dropCollection("article");
            this.mongoTargetTemplate.insert(source, Article.class);
            affectCount = source.size();
        }

        return affectCount;
    }

}
