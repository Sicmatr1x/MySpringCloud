package com.sicmatr1x.pojo;

import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 待爬取列表
 * @author sicmatr1x
 */
@Component
public class JobList {
    private List<Article> articleList;

    public JobList() {
    }

    public JobList(List<Article> articleList) {
        this.articleList = articleList;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    @Override
    public String toString() {
        return "JobList{" +
                "articleList=" + articleList +
                '}';
    }
}
