package com.sicmatr1x.controller;

import com.sicmatr1x.common.Constant;
import com.sicmatr1x.pojo.Article;
import com.sicmatr1x.service.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class SpiderController {

    @Autowired
    Constant constant;

    @Autowired
    SpiderService spiderService;

    @RequestMapping(value = "/add/zhihu/answer", method = RequestMethod.POST)
    public Article spiderZhihuAnswer(@RequestBody Article article) throws IOException {
        return spiderService.spiderZhihuAnswer(article);
    }

    @RequestMapping(value = "/add/zhihu/p", method = RequestMethod.POST)
    public Article spiderZhihuZhuanLan(@RequestBody Article article) throws IOException {
        return spiderService.spiderZhihuZhuanLan(article);
    }

    @RequestMapping(value = "/add/huxiu/article", method = RequestMethod.POST)
    public Article spiderHuxiu(@RequestBody Article article) throws IOException {
        return spiderService.spiderHuxiu(article);
    }

    @RequestMapping("/domain/{domainName}")
    public String index(@PathVariable String domainName) {
        String domain = null;
        switch (domainName) {
            case "zhihu":
                domain = constant.zhihuDomain;
                break;
            case "huxiu":
                domain = constant.huxiuDomain;
                break;
        }
        return domain;
    }
}
