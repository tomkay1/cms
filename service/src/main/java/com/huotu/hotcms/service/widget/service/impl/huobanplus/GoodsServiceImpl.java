/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.widget.service.impl.huobanplus;

import com.alibaba.fastjson.JSON;
import com.huotu.hotcms.service.util.ApiResult;
import com.huotu.hotcms.service.widget.model.Goods;
import com.huotu.hotcms.service.widget.model.GoodsSearcher;
import com.huotu.hotcms.service.widget.model.Page;
import com.huotu.hotcms.service.widget.service.GoodsService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品组件服务
 * Created by cwb on 2016/3/17.
 */
@Profile("container")
@Service
public class GoodsServiceImpl implements GoodsService {
    @Override
    public Page<Goods> searchGoods(int customerId, GoodsSearcher goodsSearcher) throws Exception{
        String content = invokeGoodsSearchProce(customerId,goodsSearcher);
        ApiResult<Page<Goods>> jsonData = JSON.parseObject(content,ApiResult.class);
        return jsonData.getData();
    }

    private String invokeGoodsSearchProce(int customerId, GoodsSearcher goodsSearcher) throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        List nameValuePairs = new ArrayList<>();
        if(goodsSearcher.getGoodsCatId()!=0) {
            nameValuePairs.add(new BasicNameValuePair("goodsCatId",Integer.toString(goodsSearcher.getGoodsCatId())));
        }
        if(goodsSearcher.getGoodsTypeId()!=0) {
            nameValuePairs.add(new BasicNameValuePair("goodsTypeId",Integer.toString(goodsSearcher.getGoodsTypeId())));
        }
        if(goodsSearcher.getBrandId()!=0) {
            nameValuePairs.add(new BasicNameValuePair("brandId",Integer.toString(goodsSearcher.getBrandId())));
        }
        if(goodsSearcher.getMinPrice()!=0) {
            nameValuePairs.add(new BasicNameValuePair("minPrice",Integer.toString(goodsSearcher.getMinPrice())));
        }
        if(goodsSearcher.getMaxPrice()!=0) {
            nameValuePairs.add(new BasicNameValuePair("maxPrice",Integer.toString(goodsSearcher.getMaxPrice())));
        }
        if(goodsSearcher.getUserId()!=0) {
            nameValuePairs.add(new BasicNameValuePair("userId",Integer.toString(goodsSearcher.getUserId())));
        }
        if(!StringUtils.isEmpty(goodsSearcher.getKeyword())) {
            nameValuePairs.add(new BasicNameValuePair("keyword",goodsSearcher.getKeyword()));
        }
        if(goodsSearcher.getPageNo()!=0) {
            nameValuePairs.add(new BasicNameValuePair("pageNo",Integer.toString(goodsSearcher.getPageNo())));
        }
        if(goodsSearcher.getDirection()!=0) {
            nameValuePairs.add(new BasicNameValuePair("direction",Integer.toString(goodsSearcher.getDirection())));
        }
        if(goodsSearcher.getSortEnum()!=null) {
            nameValuePairs.add(new BasicNameValuePair("sortEnum",(String)goodsSearcher.getSortEnum().getCode()));
        }
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost("")
                .setPath("")
                .setParameter("customerId", Integer.toString(customerId))
                .setParameters(nameValuePairs)
                .build();
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public List<Goods> getHotGoodsList(int customerId) throws Exception{
        String content = invokeHotGoodsProce(customerId);
        ApiResult<List<Goods>> jsonData = JSON.parseObject(content,ApiResult.class);
        return jsonData.getData();
    }

    private String invokeHotGoodsProce(int customerId) throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost("")
                .setPath("")
                .setParameter("customerId", Integer.toString(customerId))
                .build();
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        return EntityUtils.toString(response.getEntity());
    }
}
