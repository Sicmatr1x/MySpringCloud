package com.sicmatr1x.spider.func;

import org.jsoup.nodes.Element;

/**
 * @author sicmatr1x@outlook.com
 * @date 2023-04-14 16:15
 */
@FunctionalInterface
public interface ElementParseFunc {
    public Element parse(Element document);
}
