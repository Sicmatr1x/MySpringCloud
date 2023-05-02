package com.sicmatr1x.spider.func;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * @author sicmatr1x@outlook.com
 * @date 2023-04-14 15:39
 */
@FunctionalInterface
public interface Element2Text {
    public String getText(Element element);
}
