package com.sicmatr1x.spider.func;

import org.jsoup.nodes.Document;

/**
 * @author sicmatr1x@outlook.com
 * @date 2023-04-14 15:39
 */
@FunctionalInterface
public interface Document2Text {
    public String getText(Document document);
}
