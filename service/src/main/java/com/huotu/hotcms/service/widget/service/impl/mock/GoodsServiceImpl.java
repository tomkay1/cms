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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.json.Json;
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

    @Override
    public JsonModel searchGoods(int customerId, GoodsSearcher goodsSearcher) throws Exception{
        ApiResult<String> apiResult = invokeGoodsSearchProce(customerId,goodsSearcher);
        if(apiResult.getCode()!=200) {
            return new JsonModel();
        }
        return new Gson().fromJson(apiResult.getData(), JsonModel.class);
    }

    @Override
    public List<GoodsModel> getHotGoodsList(int customerId) throws Exception{
        ApiResult<String> apiResult = invokeHotGoodsProce(customerId);
        if(apiResult.getCode()!=200) {
            return new ArrayList<>();
        }
        return new Gson().fromJson(apiResult.getData(), new TypeToken<List<GoodsModel>>(){}.getType());
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
        return httpService.httpGet_prod("http", "api.open.fancat.cn", 8081, "/goodese/search/findTop10BySales", params);
    }
}
