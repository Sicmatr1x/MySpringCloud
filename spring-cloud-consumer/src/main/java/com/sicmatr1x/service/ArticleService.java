package com.sicmatr1x.service;

import com.sicmatr1x.pojo.Article;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
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
     * 根据URL删除文章
     * @param url
     * @return
     */
    @RequestMapping(value = "/article", method = RequestMethod.DELETE)
    public Article deleteOneArticleByURL(@RequestParam("url") String url);

    /**
     * 查找最近爬取的文章
     * @param number
     * @return
     */
    @RequestMapping(value = "/recently/articles", method = RequestMethod.GET)
    public List<Article> findRecentlyArticles(@RequestParam(value="number", required = false) Integer number);

    /**
     * 根据笔记title模糊搜索笔记
     * @param keyword
     * @param type
     * @param pageBegin
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/articles/search", method = RequestMethod.GET)
    public List<Article> searchArticles(@RequestParam(value="keyword", required = true) String keyword,
                                        @RequestParam(value="type", required = true) String type,
                                        @RequestParam(value="pageBegin", required = false) Integer pageBegin,
                                        @RequestParam(value="pageSize", required = false) Integer pageSize);

    /**
     * 添加文字笔记
     * @param article
     * @return
     */
    @RequestMapping(value = "/article/save", method = RequestMethod.POST)
    public boolean addSave(@RequestBody Article article) throws IOException;
}
