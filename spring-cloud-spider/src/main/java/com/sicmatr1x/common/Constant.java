package com.sicmatr1x.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constant {

    @Value("${constant.domain.zhihuDomain}")
    public String zhihuDomain;

    @Value("${constant.domain.huxiuDomain}")
    public String huxiuDomain;

    public String getZhihuDomain() {
        return zhihuDomain;
    }

    public String getHuxiuDomain() {
        return huxiuDomain;
    }
}
