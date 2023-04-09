package com.sicmatr1x.spider.translator;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Iterator;

public class ZhihuImgTranslator implements Translator {

  private static final Logger logger = Logger.getLogger(ZhihuImgTranslator.class);

  @Override
  public Element translate(Element element) {
    // 移除之后回答DOM下多余的noscript DOM
    Elements noScriptNode = element.select("noscript");
    if (noScriptNode == null || noScriptNode.size() == 0) {
      logger.warn("Could not get select(noscript):noScriptNode=" + noScriptNode);
      return element;
    }
    noScriptNode.first().remove();
    // 获取回答里面的img DOM
    Elements imgElements = element.select("img");

    // 遍历img DOM
    Translator img2Base64Translator = new Img2Base64Translator();
    for(Element imgElement : imgElements){
      // 转换img DOM的src从互联网URL为本地URL
      imgElement = handleImgDomAttr(imgElement);
      // img转换成base64
      imgElement = img2Base64Translator.translate(imgElement);
      int size = imgElement.parentNode().outerHtml().length();
      logger.info(" => " + size + " byte, " + (size*1.0 / 1024)/1024 + " mb");
    }
    return element;
  }

  private Element handleImgDomAttr(Element imgElement) {
    String srcAddress = null;
    // smart get img src url
    Attributes attrs = imgElement.attributes();
    Iterator<Attribute> iterator = attrs.iterator();
    while(iterator.hasNext()){
      Attribute attr = iterator.next();
      if(attr.getValue() != null && attr.getValue().contains("http")){
        srcAddress = attr.getValue();
      }
    }

    String rawWidth = imgElement.attr("data-rawwidth");
    String rawHeight = imgElement.attr("data-rawheight");
    imgElement.attr("src", srcAddress);
    imgElement.attr("style", "width:" + rawWidth + ";height:" + rawHeight);
    imgElement.removeAttr("data-caption");
    imgElement.removeAttr("data-size");
    imgElement.removeAttr("data-rawwidth");
    imgElement.removeAttr("data-rawheight");
    imgElement.removeAttr("data-default-watermark-src");
    imgElement.removeAttr("data-original");
    imgElement.removeAttr("data-actualsrc");
    return imgElement;
  }

//  @Override
//  public Element translate(Element imgElement) {
//    String srcAddress = null;
//    // smart get img src url
//    Attributes attrs = imgElement.attributes();
//    Iterator<Attribute> iterator = attrs.iterator();
//    while(iterator.hasNext()){
//      Attribute attr = iterator.next();
//      if(attr.getValue() != null && attr.getValue().contains("http")){
//        srcAddress = attr.getValue();
//      }
//    }
//
//    String rawWidth = imgElement.attr("data-rawwidth");
//    String rawHeight = imgElement.attr("data-rawheight");
//    imgElement.attr("src", srcAddress);
//    imgElement.attr("style", "width:" + rawWidth + ";height:" + rawHeight);
//    imgElement.removeAttr("data-caption");
//    imgElement.removeAttr("data-size");
//    imgElement.removeAttr("data-rawwidth");
//    imgElement.removeAttr("data-rawheight");
//    imgElement.removeAttr("data-default-watermark-src");
//    imgElement.removeAttr("data-original");
//    imgElement.removeAttr("data-actualsrc");
//    return imgElement;
//  }
}
