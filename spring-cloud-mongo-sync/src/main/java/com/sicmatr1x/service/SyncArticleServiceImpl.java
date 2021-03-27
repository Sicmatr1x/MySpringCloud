package com.sicmatr1x.service;

import com.sicmatr1x.dao.SyncArticleDao;
import com.sicmatr1x.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SyncArticleServiceImpl implements SyncArticleService{

    @Autowired
    private SyncArticleDao syncArticleDao;

    private List<Article> articleListSource;
    private List<Article> articleListTarget;

    @Override
    public String testConnectionToSourceDB(Map<String, String> mongodbMap) {
        return syncArticleDao.testConnectionToSourceDB(mongodbMap);
    }

    @Override
    public Map<String, String> readArticlesFromDB() {
        this.articleListSource = syncArticleDao.readArticlesFromSourceDB();
        this.articleListTarget = syncArticleDao.readArticlesFromTargetDB();
        Map<String, String> result = new HashMap<>();
        result.put("articleListSource.size", "" + this.articleListSource.size());
        result.put("articleListTarget.size", "" + this.articleListTarget.size());
        return result;
    }

    @Override
    public Integer margeSourceArticlesToTargetDB(boolean additionalMode) {
        int affectCount = 0;
        affectCount = syncArticleDao.margeSourceArticlesToTargetDB(additionalMode, this.articleListSource, this.articleListTarget);
        return affectCount;
    }

    @Override
    public Map<String, Object> autoMargeArticles(boolean additionalMode, Map<String, String> mongodbMap) {
        Map<String, Object> resultMap = new HashMap<>();
        // 1.尝试连接源数据库
        String sourceDBConnectionInfo = this.testConnectionToSourceDB(mongodbMap);
        resultMap.put("sourceDBConnectionInfo", sourceDBConnectionInfo);
        if (sourceDBConnectionInfo == null || "".equals(sourceDBConnectionInfo)) {
            resultMap.put("error", "Could not get DBCollection object from to source mongoDB. 无法连接源数据库");
        }
        // 2.从源数据库和目标数据库获取数据
        Map<String, String> dataRows = this.readArticlesFromDB();
        resultMap.put("dataRows", dataRows);
        // 3.开始备份数据
        Integer affectRows = this.margeSourceArticlesToTargetDB(additionalMode);
        resultMap.put("affectRows", affectRows);
        return resultMap;
    }
}
