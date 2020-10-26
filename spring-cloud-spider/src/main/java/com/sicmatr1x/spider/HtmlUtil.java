package com.sicmatr1x.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public abstract class HtmlUtil {

  protected String address;
  protected Document doc;
  protected String content;
  protected String title;

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Document getDoc() {
    return doc;
  }

  public void setDoc(Document doc) {
    this.doc = doc;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  abstract boolean initAddress();

  void getHtml() throws IOException {
    this.doc = Jsoup.connect(this.address).get();
  }

  abstract void analysisPage();

  public boolean parse() throws IOException {
    if (this.initAddress()) {
      this.getHtml();
      this.analysisPage();
      return true;
    }
    return false;
  }
}
