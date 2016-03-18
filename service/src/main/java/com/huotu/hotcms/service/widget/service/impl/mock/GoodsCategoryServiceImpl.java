/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.widget.service.impl.mock;

import com.huotu.hotcms.service.widget.model.GoodsCategory;
import com.huotu.hotcms.service.widget.service.GoodsCategoryService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类组件服务mock层
 * Created by cwb on 2016/3/17.
 */
@Profile("!container")
@Service
public class GoodsCategoryServiceImpl implements GoodsCategoryService {
    @Override
    public List<GoodsCategory> getGoodsCategories(int customerId) {
        List<GoodsCategory> goodsCategories = new ArrayList<>();
        GoodsCategory goodsCategory = new GoodsCategory();
        goodsCategory.setName("rerwnds");
        goodsCategories.add(goodsCategory);
        GoodsCategory goodsCategory1 = new GoodsCategory();
        goodsCategory1.setName("二温热微软");
        goodsCategories.add(goodsCategory1);
        return goodsCategories;
    }
}
