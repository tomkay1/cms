package com.huotu.hotcms.web.service.impl.mock;

import com.alibaba.fastjson.JSON;
import com.huotu.hotcms.service.model.Bind.AccessToken;
import com.huotu.hotcms.service.model.Bind.WxUser;
import com.huotu.hotcms.web.service.RegisterByWeixinService;
import com.huotu.huobanplus.sdk.mall.model.RegisterWeixinUserData;
import com.huotu.huobanplus.sdk.mall.service.MallInfoService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by chendeyu on 2016/4/8.
 */
@Profile("!container")
@Service
public class RegisterByWeixinImpl implements RegisterByWeixinService {
    @Autowired
    private MallInfoService mallInfoService;

    @Override
    public RegisterWeixinUserData RegisterByWeixin(int customerId,WxUser wxUser) {
        RegisterWeixinUserData registerWeixinUserData = null;
        try {
                 registerWeixinUserData = mallInfoService.registerByWeixin(customerId,wxUser.getSex(),wxUser.getNickname(),wxUser.getOpenid(),wxUser.getCity(),wxUser.getCity(),wxUser.getProvince(),wxUser.getHeadimgurl(),wxUser.getUnionid());
            } catch (IOException e) {
            e.printStackTrace();
        }

        return registerWeixinUserData;
    }

    @Override
    public WxUser getWxUser(String url) {
        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();
            URI uri=new URIBuilder(url).build();
            HttpGet httpGet = new HttpGet(uri);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int code = response.getStatusLine().getStatusCode();
            if(code == 200){
                AccessToken accessToken = JSON.parseObject(EntityUtils.toString(response.getEntity()), AccessToken.class);
                WxUser wxUser = getUserdetail(accessToken.getAccess_token(),accessToken.getOpenid());
                return wxUser;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public WxUser getUserdetail(String accessToken,String openid) throws URISyntaxException, IOException {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token="+accessToken+"&openid="+openid;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URI uri=new URIBuilder(url).build();
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        int code = response.getStatusLine().getStatusCode();
        if(code == 200){
            WxUser wxUser = JSON.parseObject(EntityUtils.toString(response.getEntity()), WxUser.class);
            return wxUser;
        }
        else {
            return  null;
        }

    }
}
