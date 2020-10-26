package com.sicmatr1x.service;

import com.sicmatr1x.pojo.Article;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name= "spring-cloud-mongo-producer")
public interface ArticleService {

    @RequestMapping(value = "/article/save", method = RequestMethod.POST)
    public void saveArticle(@RequestBody Article article);

    @RequestMapping(value = "/article", method = RequestMethod.GET)
    public Article findOneArticleByURL(@RequestParam("url") String url);

    @RequestMapping(value = "/recently/articles", method = RequestMethod.GET)
    public List<Article> findRecentlyArticles(@RequestParam(value = "number", required = false) Integer number);

}
