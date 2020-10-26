package com.sicmatr1x.spider.translator;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Iterator;

public class HuxiuImgTranslator implements Translator{
    @Override
    public Element translate(Element element) {
        // 获取回答里面的img DOM
        Elements imgElements = element.select("img");

        // 遍历img DOM
        Translator img2Base64Translator = new Img2Base64Translator();
        for(Element imgElement : imgElements){
            // 转换img DOM的src从互联网URL为本地URL
            imgElement = handleImgDomAttr(imgElement);
            // img转换成base64
            imgElement = img2Base64Translator.translate(imgElement);
        }

        // 移除之后回答DOM下多余的noscript DOM
//        Elements noscriptNode = element.select(".img-center-box");
//        if(noscriptNode.size() == 0){
//            return element;
//        }
//        noscriptNode.first().remove();
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
}
