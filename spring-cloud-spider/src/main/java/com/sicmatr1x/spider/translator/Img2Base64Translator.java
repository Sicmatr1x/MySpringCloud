package com.sicmatr1x.spider.translator;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.binary.Base64;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

public class Img2Base64Translator implements Translator {
    @Override
    public Element translate(Element imgElement) {
        String srcAddress = null;
        Attributes attrs = imgElement.attributes();
        Iterator<Attribute> iterator = attrs.iterator();
        while(iterator.hasNext()){
            Attribute attr = iterator.next();
            if(attr.getValue() != null && attr.getValue().contains("http")){
                srcAddress = attr.getValue();
            }
        }
        imgElement.attr("alt", srcAddress);
        System.out.print(srcAddress);
        String base64 = imageToBase64(srcAddress);
        String imgType = getFileType(srcAddress);
        imgElement.attr("src", "data:image/" + imgType + ";base64," + base64);
        return imgElement;
    }

    private String getFileType(String srcAddress) {
        String[] work = srcAddress.split("\\.");
        if (work.length > 0) {
            return work[work.length-1];
        } else {
            return "png";
        }
    }

    /**
     * 在线图片转换成base64字符串
     * https://segmentfault.com/q/1010000009065824
     * @param imgURL 图片线上路径
     * @return
     */
    public static String imageToBase64(String imgURL) {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            // 创建URL
            URL url = new URL(imgURL);
            byte[] by = new byte[1024];
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream is = conn.getInputStream();
            // 将内容读取内存中
            int len = -1;
            while ((len = is.read(by)) != -1) {
                data.write(by, 0, len);
            }
            int size = data.size();
            if (size > 1*1024*1024) {
                double multpu = 1024*1024*1.0 / size;
//                ByteArrayInputStream swapStream = new ByteArrayInputStream(data.toByteArray());
//                BufferedImage originalImage = ImageIO.read(swapStream);
                data.reset();
                Thumbnails.fromURLs(Collections.singleton(new URL(imgURL))).scale(1f).outputQuality(0.1f).toOutputStream(data);
            }
            // 关闭流
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        Base64 encoder = new Base64();
        return encoder.encodeToString(data.toByteArray());
    }

    public static String imageToBase64LimitSize(String imgURL) {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            Thumbnails.of(new URL(imgURL)).scale(0.5f).toOutputStream(data);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        Base64 encoder = new Base64();
        return encoder.encodeToString(data.toByteArray());
    }

    /**
     * 在线图片转换成base64字符串
     * https://segmentfault.com/q/1010000009065824
     * @param imgURL 图片线上路径
     * @return
     */
    public static String imageToBase64ByOnline(String imgURL) {
        if (imgURL == null || "".equals(imgURL) ) {
            return null;
        }

        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // 创建URL
            URL url = new URL(imgURL);

            // 创建链接
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            is = conn.getInputStream();

            // 将内容读取内存中
            int len = -1;
            byte[] by = new byte[1024];
            while ((len = is.read(by)) != -1) {
                out.write(by, 0, len);
            }

            byte[] dataByte = out.toByteArray();
            //如果 图片经过nginx就会被压缩，返回头中包含 Content-Encoding:gzip 则需要对图片数据解压缩
            String contentEncoding = conn.getContentEncoding();
            if("gzip".equalsIgnoreCase(contentEncoding)){
                dataByte = uncompress(dataByte);
            }

            // 对字节数组Base64编码
            Base64 encoder = new Base64();
            return encoder.encodeToString(dataByte);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                // 关闭流
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    is = null;
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

        return null;
    }

    /**
     * GZIP解压缩
     *
     * @param bytes 压缩内容
     * @return
     */
    public static byte[] uncompress(byte[] bytes) throws IOException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPInputStream ungzip = null;
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[1024];
            int n = 0;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            ungzip.close(); //这个会写入一些数据，所以要在out使用前调用

            return out.toByteArray();
        } finally {
            out.close();
        }
    }
}
