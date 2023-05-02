package com.sicmatr1x.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * 文章爬取来源
 * @author sicmatr1x
 */

public enum ArticleSource {
    /**
     * 知乎回答
     */
    ZHIHU_ANSWER,
    /**
     * 知乎专栏
     */
    ZHIHU_ZHUANLAN,
    /**
     * 虎嗅文章
     */
    HUXIU;

    public static final Map<String, String> supportDomains = new HashMap<>();

    static {
        supportDomains.put("https://www.zhihu.com/", "ZHIHU_ANSWER");
        supportDomains.put("https://zhuanlan.zhihu.com/", "ZHIHU_ZHUANLAN");
        supportDomains.put("https://www.huxiu.com/", "HUXIU");
    }

}
