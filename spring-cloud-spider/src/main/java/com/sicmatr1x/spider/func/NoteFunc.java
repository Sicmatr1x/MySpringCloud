package com.sicmatr1x.spider.func;

import org.jsoup.nodes.Element;

import java.util.Map;

/**
 * @author sicmatr1x@outlook.com
 * @date 2023-04-14 16:52
 */
@FunctionalInterface
public interface NoteFunc {
    public String generate(Element element, Map<String, String> params);
}
