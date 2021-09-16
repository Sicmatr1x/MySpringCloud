package com.sicmatr1x.spider;

import com.sicmatr1x.spider.translator.HeadTitleTranslator;
import com.sicmatr1x.spider.translator.ImgDownloader;
import com.sicmatr1x.spider.translator.Translator;
import com.sicmatr1x.spider.translator.ZhihuImgTranslator;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class ZhihuZhuanlanHtmlUtil extends HtmlUtil{

  public static final String DOMAIN = "https://zhuanlan.zhihu.com/";

  @Override
  boolean initAddress() {
    if (this.address == null || this.address.length() < DOMAIN.length()) {
      System.out.print("Address is too short:" + this.address);
      return false;
    }
    return true;
  }

  /**
   * 获取img dom
   * @param element
   * @return
   */
  public Element translateImgDom(Element element) {
    element.select("noscript").first().remove();
    Elements imgElements = element.select("img");
    ZhihuImgTranslator zhihuImgTranslator = new ZhihuImgTranslator();
    for(Element imgElement : imgElements){
      imgElement = zhihuImgTranslator.translate(imgElement);
      this.downloadImg(imgElement);
    }
    return element;
  }

  private Element downloadImg(Element element){
    ImgDownloader imgDownloader = new ImgDownloader();
    return imgDownloader.translate(element, this.title + "_files");
  }

  public Element getArticle() {
    // TODO
    Elements articleRichText = this.doc.select(".Post-RichText");
    if (articleRichText.isEmpty()){
      System.out.println("error: can not found class=\"Post-RichText\" in " + this.address);
    }else{
      return articleRichText.first();
    }
    return null;
  }

  public String initTitle() {
    Elements question = this.doc.select(".Post-Title");
    if (question.isEmpty()) {
      question = this.doc.select(".QuestionHeader-title");
    }
    return question.get(0).text();
  }

  @Override
  void analysisPage() {
    HeadTitleTranslator headTitleTranslator = new HeadTitleTranslator();
    headTitleTranslator.setFromAddress(this.getAddress());

    int endIndex = this.address.indexOf("/", DOMAIN.length());
    String mode = this.address.substring(DOMAIN.length(), endIndex);
    this.title = this.initTitle();
    headTitleTranslator.setTitleText(this.title);

    if ("p".equals(mode)) {
      Element answerElement = this.getArticle(); // 单个回答链接
      Translator zhihuImgTranslator = new ZhihuImgTranslator();
      answerElement = zhihuImgTranslator.translate(answerElement);
      this.content = headTitleTranslator.translate(answerElement).html();
    } else { // 文章链接
      System.out.println("ZhihuZhuanlanHtmlUtil not support for:" + this.address);
    }
  }
}
