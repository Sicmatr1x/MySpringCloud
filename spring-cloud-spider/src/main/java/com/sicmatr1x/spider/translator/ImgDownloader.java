package com.sicmatr1x.spider.translator;

import org.jsoup.nodes.Element;

import java.io.*;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;

public class ImgDownloader {

  private static void downloadPicture(String urlList, String path) throws MalformedURLException, ConnectException{
    URL url = null;
    try {
      File imgFile= new File(path);
      File mediaFolder = imgFile.getParentFile();
      if (!mediaFolder.exists()) {
        mediaFolder.mkdirs();
      }

      url = new URL(urlList);
      DataInputStream dataInputStream = new DataInputStream(url.openStream());

      FileOutputStream fileOutputStream = new FileOutputStream(imgFile);
      ByteArrayOutputStream output = new ByteArrayOutputStream();

      byte[] buffer = new byte[1024];
      int length;

      while ((length = dataInputStream.read(buffer)) > 0) {
        output.write(buffer, 0, length);
      }
      fileOutputStream.write(output.toByteArray());
      dataInputStream.close();
      fileOutputStream.close();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   *
   * @param url img url like: https://pic3.zhimg.com/v2-9e02be59739818c9029d028b758d5c9a_r.jpg
   * @return
   */
  private String getImgName(String url){
    String[] work = url.split("/");
    return work[work.length - 1];
  }

  public Element translate(Element element, String folderName){
    String srcAddress = element.attr("src");
    String imgName = this.getImgName(srcAddress);
//    String imgRelativePath = FilenameChecker.getLegalFileName(folderName) + "/" + imgName;
//    System.out.println("downloading img:" + imgRelativePath);
//    try {
//      downloadPicture(srcAddress, Setting.getSetting().getDownloadPath() + "/" + imgRelativePath);
//    } catch (MalformedURLException | ConnectException e) {
//      element.attr("src", srcAddress);
//      element.attr("alt", srcAddress);
//      return element;
//    }
//    element.attr("src", imgRelativePath);
//    element.attr("alt", srcAddress);
    return element;
  }
}
