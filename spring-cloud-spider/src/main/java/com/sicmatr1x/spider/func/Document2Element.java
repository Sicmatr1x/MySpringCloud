package com.sicmatr1x.spider.func;

import org.jsoup.nodes.Element;

/**
 * @author sicmatr1x@outlook.com
 * @date 2023-04-14 16:06
 */
@FunctionalInterface
public interface Document2Element {
    public Element get(Element document);
}
