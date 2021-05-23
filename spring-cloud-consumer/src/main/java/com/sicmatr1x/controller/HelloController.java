package com.sicmatr1x.controller;

import com.sicmatr1x.service.HelloMongoProducerService;
import com.sicmatr1x.service.HelloSpiderService;
import com.sicmatr1x.vo.CommonVo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    HelloMongoProducerService helloMongoProducerService;

    @Autowired
    HelloSpiderService helloSpiderService;

    @ApiOperation(
            value = "测试Consumer微服务与其它微服务联通的接口",
            notes = "其它微服务名，例如：spring-cloud-mongo-producer, spring-cloud-spider",
            httpMethod = "GET",
            consumes = "*/*",
            protocols = "http",
            produces = "application/json;charset=UTF-8",
            response = CommonVo.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",name = "serviceName",value = "其它微服务名",dataType = "String",defaultValue = "spring-cloud-mongo-producer",required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Pet not found")
    })
    @GetMapping("/hello/{serviceName}")
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
            case "spring-cloud-consumer":
                result = "Current microservice name already is spring-cloud-consumer, please enter other microservice name";
                break;
            default:
                result = "Please enter right microservice name";
        }
        commonVo.setData(result);
        return commonVo;
    }

}