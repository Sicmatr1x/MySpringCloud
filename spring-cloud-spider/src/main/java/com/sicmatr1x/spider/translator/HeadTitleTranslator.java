package com.sicmatr1x.spider.translator;

import org.jsoup.nodes.Element;

public class HeadTitleTranslator implements Translator {

    private String titleText;

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    private String fromAddress;

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    @Override
    public Element translate(Element element) {
        Element head = new Element("head");
        Element title = new Element("title");
        title.text(this.titleText);
        title.appendTo(head);

        // 笔记信息
        Element noteTitle = new Element("h1");
        noteTitle.text(this.titleText);
        Element fromLink = new Element("a");
        fromLink.text(this.titleText);
        fromLink.attr("href", this.fromAddress);

        Element body = new Element("body");
        noteTitle.appendTo(body);
        fromLink.appendTo(body);
        element.appendTo(body);

        Element html = new Element("html");
        head.appendTo(html);
        body.appendTo(html);
        return html;
    }
}
