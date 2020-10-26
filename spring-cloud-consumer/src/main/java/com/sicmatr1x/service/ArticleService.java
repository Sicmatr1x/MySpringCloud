package com.sicmatr1x.service;

import com.sicmatr1x.pojo.Article;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name= "spring-cloud-mongo-producer")
public interface ArticleService {
    /**
     * 根据URL查找文章
     * @param url
     * @return
     */
    @RequestMapping(value = "/article", method = RequestMethod.GET)
    public Article findOneArticleByURL(@RequestParam("url") String url);

    /**
     * 查找最近爬取的文章
     * @param number
     * @return
     */
    @RequestMapping(value = "/recently/articles", method = RequestMethod.GET)
    public List<Article> findRecentlyArticles(@RequestParam(value="number", required = false) Integer number);
}
