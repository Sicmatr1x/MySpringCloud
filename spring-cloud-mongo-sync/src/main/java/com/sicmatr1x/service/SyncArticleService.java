package com.sicmatr1x.service;

import com.sicmatr1x.pojo.Article;
import com.sicmatr1x.vo.CommonVo;

import java.util.List;
import java.util.Map;

public interface SyncArticleService {

    /**
     * 从源数据库读取数据
     * @return document Article数据
     */
    public String testConnectionToSourceDB(Map<String, String> mongodbMap);

    /**
     * 从目标数据库读取数据
     * @return document 读取到的Article数据条数
     */
    public Map<String, String> readArticlesFromDB();

    /**
     * 将源MongoDB的数据合并到目标db
     * @param additionalMode 是否为追加模式，true追加，false覆盖
     * @return 新增记录条数
     */
    public Integer margeSourceArticlesToTargetDB(boolean additionalMode);

    /**
     * 自动执行备份源数据库article document到目标数据库操作
     * @param additionalMode 是否为追加模式，true追加，false覆盖
     * @param mongodbMap mongodbMap MongoDB登录信息
     * @return 操作信息
     */
    public Map<String, Object> autoMargeArticles(boolean additionalMode, Map<String, String> mongodbMap);
}
