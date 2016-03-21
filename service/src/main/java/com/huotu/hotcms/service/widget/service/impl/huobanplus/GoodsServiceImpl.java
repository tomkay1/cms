/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.widget.service.impl.huobanplus;

import com.huotu.hotcms.service.widget.model.Goods;
import com.huotu.hotcms.service.widget.model.GoodsSearcher;
import com.huotu.hotcms.service.widget.model.Page;
import com.huotu.hotcms.service.widget.service.GoodsService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品组件服务
 * Created by cwb on 2016/3/17.
 */
@Profile("container")
@Service
public class GoodsServiceImpl implements GoodsService {
    @Override
    public Page<Goods> searchGoods(int customerId, GoodsSearcher goodsSearcher) {
        return null;
    }

    @Override
    public List<Goods> getHotGoodsList(int customerId) {
        return null;
    }
}
