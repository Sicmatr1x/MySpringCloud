package com.sicmatr1x.controller;

import com.sicmatr1x.service.HelloMongoProducerService;
import com.sicmatr1x.service.HelloSpiderService;
import com.sicmatr1x.vo.CommonVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    HelloMongoProducerService helloMongoProducerService;

    @Autowired
    HelloSpiderService helloSpiderService;
	
    @RequestMapping("/hello/{serviceName}")
    public CommonVo index(@PathVariable("serviceName") String serviceName) {
        CommonVo commonVo = new CommonVo(true);
        String result = null;
        switch (serviceName) {
            case "spring-cloud-mongo-producer":
                result =  helloMongoProducerService.hello("spring-cloud-consumer");
                break;
            case "spring-cloud-spider":
                result =  helloSpiderService.hello("spring-cloud-consumer");
                break;
        }
        commonVo.setData(result);
        return commonVo;
    }

}