package com.sicmatr1x.dao;

import com.sicmatr1x.pojo.Article;

import java.util.List;
import java.util.Map;

public interface SyncArticleDao {

    /**
     *
     * @param mongodbMap MongoDB登录信息
     * @return
     */
    public String testConnectionToSourceDB(Map<String, String> mongodbMap);

    /**
     * 从源数据库读取数据
     * @return document Article数据
     */
    public List<Article> readArticlesFromSourceDB();

    /**
     * 从目标数据库读取数据
     * @return document Article数据
     */
    public List<Article> readArticlesFromTargetDB();

    /**
     * 将源MongoDB的数据合并到目标db
     * @param additionalMode 是否为追加模式，true追加，false覆盖
     * @return 新增记录条数
     */
    public Integer margeSourceArticlesToTargetDB(boolean additionalMode, List<Article> source, List<Article> target);
}
