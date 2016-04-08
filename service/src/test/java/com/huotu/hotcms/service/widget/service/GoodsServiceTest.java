/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.widget.service;

import com.huotu.hotcms.service.config.ServiceTestConfig;
import com.huotu.hotcms.service.widget.model.GoodsSearcher;
import com.huotu.huobanplus.common.entity.Goods;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by cwb on 2016/4/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceTestConfig.class)
@WebAppConfiguration
@Transactional
public class GoodsServiceTest {

    @Autowired
    private GoodsService goodsService;

    @Test
    public void searchGoodsTest() throws Exception{
        Page<Goods> jsonModel = goodsService.searchGoods(4471,new GoodsSearcher());
        System.out.print(jsonModel);
    }

    @Test
    public void getHotGoodsListTest() {

    }
}
