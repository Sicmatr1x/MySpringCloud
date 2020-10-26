package com.sicmatr1x.service;

import com.sicmatr1x.pojo.Article;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * Feign是一个声明式Web Service客户端。
 * 使用Feign能让编写Web Service客户端更加简单, 它的使用方法是定义一个接口，
 * 然后在上面添加注解，同时也支持JAX-RS标准的注解。
 * Feign也支持可拔插式的编码器和解码器。
 * Spring Cloud对Feign进行了封装，使其支持了Spring MVC标准注解和HttpMessageConverters。
 * Feign可以与Eureka和Ribbon组合使用以支持负载均衡。
 *
 * name:远程服务名，即spring.application.name配置的名称
 */
@FeignClient(name= "spring-cloud-spider")
public interface SpiderService {
    /**
     * 爬取知乎回答
     * @param article
     * @return
     */
    @RequestMapping(value = "/add/zhihu/answer", method = RequestMethod.POST)
    public Article spiderZhihuAnswer(@RequestBody Article article) throws IOException;

    /**
     * 爬取知乎文章/专栏
     * @param article
     * @return
     */
    @RequestMapping(value = "/add/zhihu/p", method = RequestMethod.POST)
    public Article spiderZhihuZhuanLan(@RequestBody Article article) throws IOException;

    /**
     * 爬取虎嗅文章
     * @param article
     * @return
     */
    @RequestMapping(value = "/add/huxiu/article", method = RequestMethod.POST)
    public Article spiderHuxiu(@RequestBody Article article) throws IOException;

    @RequestMapping(value = "/domain/{domainName}")
    public String getDomainURLByName(@PathVariable("domainName") String domainName);
}
