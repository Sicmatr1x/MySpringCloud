package com.sicmatr1x.spider.func;

import org.jsoup.nodes.Document;

/**
 * @author sicmatr1x@outlook.com
 * @date 2023-04-14 14:30
 */
@FunctionalInterface
public interface DocumentParseFunc {

    public Document parse(Document document);
}
