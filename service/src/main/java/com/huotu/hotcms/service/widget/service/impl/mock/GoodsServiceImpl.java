/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.widget.service.impl.mock;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.huotu.hotcms.service.service.HttpService;
import com.huotu.hotcms.service.util.ApiResult;
import com.huotu.hotcms.service.widget.model.GoodsModel;
import com.huotu.hotcms.service.widget.model.GoodsSearcher;
import com.huotu.hotcms.service.widget.model.JsonModel;
import com.huotu.hotcms.service.widget.service.GoodsService;
import com.huotu.huobanplus.common.entity.Goods;
import com.huotu.huobanplus.sdk.common.repository.GoodsRestRepository;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.json.Json;
import java.awt.geom.Area;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 商品组件服务mock层
 * Created by cwb on 2016/3/17.
 */
@Profile("!container")
@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private HttpService httpService;
    @Autowired
    private GoodsRestRepository goodsRestRepository;
    @Autowired
    private Environment environment;

    @Override
    public Page<Goods> searchGoods(int customerId, GoodsSearcher goodsSearcher) throws Exception{

//        Sort.Direction direction = getSortDirection(goodsSearcher);
//        String[] properties = getSortProperties(goodsSearcher);
        String hostName = environment.getProperty("com.huotu.huobanplus.open.api.root", "api.open.fancat.cn");
        String appid = environment.getProperty("com.huotu.huobanplus.open.api.appid","_demo");
        String appsecrect = environment.getProperty("com.huotu.huobanplus.open.api.appsecrect","1f2f3f4f5f6f7f8f");
        try(CloseableHttpClient client = goodsRestRepository.getHttpClient()) {
            HttpHost httpHost = new HttpHost(hostName,8081);

//            client.execute(httpHost,)
        }
        Page<Goods> goodses = goodsRestRepository.search(customerId,
                goodsSearcher.getGoodsCatId(),
                goodsSearcher.getGoodsTypeId(),
                goodsSearcher.getBrandId(),
                goodsSearcher.getMinPrice(),
                goodsSearcher.getMaxPrice(),
                goodsSearcher.getUserId(),
                goodsSearcher.getKeyword(),
                new PageRequest(goodsSearcher.getPage(),20));
        return goodses;
    }

    @Override
    public List<Goods> getHotGoodsList(int customerId) throws Exception{
        List<Goods> goodses = goodsRestRepository.searchTop10Sales(customerId);
        return goodses;
    }

    private ApiResult<String> invokeGoodsSearchProce(int customerId, GoodsSearcher goodsSearcher) throws Exception{
        Map<String,Object> params = buildSortedParams(customerId,goodsSearcher);
        return httpService.httpGet_prod("http", "api.open.fancat.cn", 8081, "/goodses/search/findByMixed", params);
    }

    private Map<String, Object> buildSortedParams(int customerId, GoodsSearcher goodsSearcher) throws Exception{
        Map<String,Object> params = new TreeMap<>();
        params.put("merchantId",customerId);
        Class clazz = goodsSearcher.getClass();
        Field[] fields = clazz.getFields();
        for(Field field : fields) {
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(),clazz);
            Method method = pd.getReadMethod();
            params.put(field.getName(),method.invoke(goodsSearcher));
        }
        return params;
    }

    private ApiResult<String> invokeHotGoodsProce(int customerId) throws Exception{
        Map<String,Object> params = new TreeMap<>();
        params.put("merchantId",customerId);
        return httpService.httpGet_prod("http", "api.open.fancat.cn", 8081, "/goodses/search/findTop10BySales", params);
    }
}
