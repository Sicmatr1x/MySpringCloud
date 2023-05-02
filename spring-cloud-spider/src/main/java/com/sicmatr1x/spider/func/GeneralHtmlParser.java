package com.sicmatr1x.spider.func;

import org.jsoup.select.Elements;

/**
 * @author sicmatr1x@outlook.com
 * @date 2023-04-14 16:42
 */
public class GeneralHtmlParser {
    /**
     * parse title from html
     */
    public static final Document2Text parseTitle = (doc) -> {
        Elements titleNode = doc.select("title");
        if (titleNode == null) {
            return null;
        }
        return titleNode.text();
    };



}
