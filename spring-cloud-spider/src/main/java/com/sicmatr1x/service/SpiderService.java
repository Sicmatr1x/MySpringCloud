package com.sicmatr1x.service;

import com.sicmatr1x.pojo.Article;

import java.io.IOException;


public interface SpiderService {

    public Article spiderURL(Article article) throws IOException;

    public Article spiderZhihuAnswer(Article article) throws IOException;

    public Article spiderZhihuZhuanLan(Article article) throws IOException;

    public Article spiderHuxiu(Article article) throws IOException;
}
