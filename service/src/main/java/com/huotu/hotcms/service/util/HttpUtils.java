package com.huotu.hotcms.service.util;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    public static String httpGet(String scheme,String host,String path, Map<String, Object> params) throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URIBuilder uriBuilder = new URIBuilder()
                .setScheme(scheme)
                .setHost(host)
                .setPath(path);
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        params.forEach((key,value)->{
            if(value != null) {
                nameValuePairs.add(new BasicNameValuePair(key,String.valueOf(value)));
            }
        });
        URI uri = uriBuilder.setParameters(nameValuePairs).build();
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        return EntityUtils.toString(response.getEntity());
    }
}
