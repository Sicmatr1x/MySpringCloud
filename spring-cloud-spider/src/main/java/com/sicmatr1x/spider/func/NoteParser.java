package com.sicmatr1x.spider.func;

import org.jsoup.nodes.Element;

/**
 * @author sicmatr1x@outlook.com
 * @date 2023-04-14 16:41
 */
public class NoteParser {

    public static final NoteFunc note = (element, params) -> {
        Element head = new Element("head");
        Element title = new Element("title");
        title.text(params.get("title"));
        title.appendTo(head);

        // 笔记信息
        Element noteTitle = new Element("h1");
        noteTitle.text(params.get("title"));
        Element fromLink = new Element("a");
        fromLink.text(params.get("title"));
        fromLink.attr("href", params.get("url"));

        Element body = new Element("body");
        noteTitle.appendTo(body);
        fromLink.appendTo(body);
        element.appendTo(body);

        Element htmlDom = new Element("html");
        head.appendTo(htmlDom);
        body.appendTo(htmlDom);
        return htmlDom.html();
    };
}
