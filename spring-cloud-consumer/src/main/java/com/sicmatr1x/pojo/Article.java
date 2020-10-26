package com.sicmatr1x.pojo;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * 文章实体类
 * @author sicmatr1x
 */
public class Article implements Serializable {
    private String id;
    private Date createdTime;
    private String url;
    private ArticleSource source;
    private String title;
    private String body;
    private String markdown;
    private String[] tags;
    private String category;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArticleSource getSource() {
        return source;
    }

    public void setSource(ArticleSource source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMarkdown() {
        return markdown;
    }

    public void setMarkdown(String markdown) {
        this.markdown = markdown;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id='" + id + '\'' +
                ", createdTime=" + createdTime +
                ", url='" + url + '\'' +
                ", source=" + source +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", markdown='" + markdown + '\'' +
                ", tags=" + Arrays.toString(tags) +
                ", category='" + category + '\'' +
                '}';
    }
}
