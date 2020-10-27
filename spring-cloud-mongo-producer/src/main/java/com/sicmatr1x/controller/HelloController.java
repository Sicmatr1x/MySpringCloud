package com.sicmatr1x.controller;


import com.sicmatr1x.vo.CommonVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	
    @RequestMapping("/hello")
    public CommonVo index(@RequestParam String callFrom) {
        CommonVo commonVo = new CommonVo(true);
        String result = "spring-cloud-mongo-producer";
        commonVo.setData(result);
        return commonVo;
    }
}