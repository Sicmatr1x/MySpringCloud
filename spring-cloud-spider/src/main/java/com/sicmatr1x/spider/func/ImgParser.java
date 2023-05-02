package com.sicmatr1x.spider.func;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.binary.Base64;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author sicmatr1x@outlook.com
 * @date 2023-04-14 16:32
 */
public class ImgParser {

    public static final ElementParseFunc imgDomBase64 = (imgElement) -> {
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
    };

    private static String getFileType(String srcAddress) {
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
                // 图片压缩
                // 按照比例因子进行缩放，不指定高宽的具体大小 scale 比例 scale取值越大，比例越高，1f代表原比例，0.5f代表原比例的一半
                // 图片的压缩质量 outputQuality是图片的质量，值是在0到1，越接近于1质量越好，越接近于0质量越差。
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
}
