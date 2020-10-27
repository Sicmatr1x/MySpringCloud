package com.sicmatr1x.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name= "spring-cloud-mongo-producer")
public interface HelloMongoProducerService {
    @RequestMapping(value = "/hello")
    public String hello(@RequestParam(value = "callFrom") String callFrom);
}
