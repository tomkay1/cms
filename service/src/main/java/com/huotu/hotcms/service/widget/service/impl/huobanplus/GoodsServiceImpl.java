/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.widget.service.impl.huobanplus;

import com.alibaba.fastjson.JSON;
import com.huotu.hotcms.service.common.SysConstant;
import com.huotu.hotcms.service.util.ApiResult;
import com.huotu.hotcms.service.util.HttpUtils;
import com.huotu.hotcms.service.util.SignBuilder;
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
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
        Map<String,Object> params = buildSortedParams(customerId,goodsSearcher);
        String sign = SignBuilder.buildSignIgnoreEmpty(params, null, SysConstant.WIDGET_KEY);
        params.put("sign",sign);
        return HttpUtils.httpGet("http", "", "", params);
    }

    private Map<String, Object> buildSortedParams(int customerId, GoodsSearcher goodsSearcher) throws Exception{
        Map<String,Object> params = new TreeMap<>();
        params.put("customerId",customerId);
//        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(goodsSearcher.getClass());
//        for(PropertyDescriptor property : propertyDescriptors) {
//            params.put(property.getName(),property.getValue(property.getName()));
//        }
        Field[] fields = goodsSearcher.getClass().getFields();
        for(Field field : fields) {
            field.setAccessible(true);
            params.put(field.getName(),field.get(field.getName()));
        }
        return params;
    }

    @Override
    public List<Goods> getHotGoodsList(int customerId) throws Exception{
        String content = invokeHotGoodsProce(customerId);
        ApiResult<List<Goods>> jsonData = JSON.parseObject(content,ApiResult.class);
        return jsonData.getData();
    }

    private String invokeHotGoodsProce(int customerId) throws Exception{
        Map<String,Object> params = new TreeMap<>();
        params.put("customerId",customerId);
        String sign = SignBuilder.buildSignIgnoreEmpty(params, null, SysConstant.WIDGET_KEY);
        params.put("sign",sign);
        return HttpUtils.httpGet("http", "", "", params);
    }
}
