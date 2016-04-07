/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.widget.service.impl.huobanplus;

import com.alibaba.fastjson.JSON;
import com.huotu.hotcms.service.service.HttpService;
import com.huotu.hotcms.service.util.ApiResult;
import com.huotu.hotcms.service.util.HttpUtils;
import com.huotu.hotcms.service.widget.model.GoodsCategory;
import com.huotu.hotcms.service.widget.service.GoodsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 商品分类组件服务
 * Created by cwb on 2016/3/21.
 */
@Profile("container")
@Service
public class GoodsCategoryServiceImpl implements GoodsCategoryService {

    @Autowired
    private HttpService httpService;

    @Override
    public List<GoodsCategory> getGoodsCategories(int customerId) {
        ApiResult<String> apiResult = invokeGoodsCatgProce(customerId);
        if(apiResult.getCode()!=200) {
            return new ArrayList<>();
        }
        return JSON.parseArray(apiResult.getData(), GoodsCategory.class);
    }

    private ApiResult<String> invokeGoodsCatgProce(Integer customerId) {
        Map<String,Object> params = new TreeMap<>();
        params.put("merchantId",customerId);
        return httpService.httpGet_prod("http", "api.open.huobanplus.com", null, "/categories/search/findByMerchantId", params);
    }
}
