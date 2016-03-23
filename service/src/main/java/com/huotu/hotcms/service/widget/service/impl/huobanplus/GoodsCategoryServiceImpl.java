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
import com.huotu.hotcms.service.util.HttpUtils;
import com.huotu.hotcms.service.widget.model.GoodsCategory;
import com.huotu.hotcms.service.widget.service.GoodsCategoryService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

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
    @Override
    public List<GoodsCategory> getGoodsCategories(int customerId) throws Exception{
        ApiResult<String> apiResult = invokeGoodsCatgProce(customerId);
        if(apiResult.getCode()!=200) {
            throw new Exception(apiResult.getMsg());
        }
        List<GoodsCategory> goodsCategories = JSON.parseArray(apiResult.getData(), GoodsCategory.class);
        return goodsCategories;
    }

    private ApiResult<String> invokeGoodsCatgProce(Integer customerId) throws Exception {
        Map<String,Object> params = new TreeMap<>();
        params.put("customerId",customerId);
        return HttpUtils.httpGet_prod("http", "api.open.huobanplus.com", null, "", params);
    }
}
