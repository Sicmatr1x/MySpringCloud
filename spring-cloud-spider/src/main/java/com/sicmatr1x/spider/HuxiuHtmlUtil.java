package com.sicmatr1x.spider;

import com.sicmatr1x.spider.translator.HeadTitleTranslator;
import com.sicmatr1x.spider.translator.HuxiuImgTranslator;
import com.sicmatr1x.spider.translator.Translator;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class HuxiuHtmlUtil extends HtmlUtil{

    public static final String DOMAIN = "https://www.huxiu.com/article/";

    @Override
    boolean initAddress() {
        if (this.address == null || this.address.length() < DOMAIN.length()) {
            System.out.print("Address is too short:" + this.address);
            return false;
        }
        return true;
    }

    public String initTitle() {
        Elements question = this.doc.select(".article__title");
        return question.get(0).text();
    }

    public Element getArticle() {
        // TODO
        Elements articleRichText = this.doc.select(".article__content");
        if (articleRichText.isEmpty()){
            System.out.println("error: can not found class=\"Post-RichText\" in " + this.address);
        }else{
            return articleRichText.first();
        }
        return null;
    }

    @Override
    void analysisPage() {
        HeadTitleTranslator headTitleTranslator = new HeadTitleTranslator();
        headTitleTranslator.setFromAddress(this.getAddress());

        this.title = this.initTitle();
        headTitleTranslator.setTitleText(this.title);
        Element answerElement = this.getArticle();
        Translator huxiuImgTranslator = new HuxiuImgTranslator();
        answerElement = huxiuImgTranslator.translate(answerElement);
        this.content = headTitleTranslator.translate(answerElement).html();
    }
}
