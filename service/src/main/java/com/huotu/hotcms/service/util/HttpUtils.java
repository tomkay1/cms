package com.huotu.hotcms.service.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/3/17.
 */
public class HttpUtils {
    /**
     * 通过图片url返回图片Bitmap
     * @param url
     * @return
     */
    public static InputStream getInputStreamByUrl(URL url) throws MalformedURLException ,IOException {
        InputStream is = null;
        if (url != null) {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();//利用HttpURLConnection对象,我们可以从网络中获取网页数据.
            conn.setDoInput(true);
            conn.connect();
//            conn.setRequestProperty("Charset", "utf-8");
            is = conn.getInputStream(); //得到网络返回的输入流
//            DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
//            is.writeBytes("tqpadmac="+URLEncoder.encode("B407F9D67C80", "utf-8"));
//            is.writeBytes("tqpadver="+URLEncoder.encode("1", "utf-8"));
        }
        return is;
    }

    public static String getHtmlByUrl(URL url) throws IOException {
        String html="";
        InputStream inputStream=getInputStreamByUrl(url);
        byte[] getData = readInputStream(inputStream); //获得网站的二进制数据
        html = new String(getData,"utf-8");
        return html;
    }

    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}
