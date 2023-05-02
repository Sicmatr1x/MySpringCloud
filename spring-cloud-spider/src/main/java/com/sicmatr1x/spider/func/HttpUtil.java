package com.sicmatr1x.spider.func;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * @author sicmatr1x@outlook.com
 * @date 2023-04-14 14:56
 */
public class HttpUtil {
    /**
     * get html from url
     * @param url html url
     * @return jsoup document object
     * @throws IOException not found
     */
    public static Document get(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
}
