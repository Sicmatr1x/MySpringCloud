package com.sicmatr1x.spider.translator;


import org.jsoup.nodes.Element;

public interface Translator {
  Element translate(Element element);
}
