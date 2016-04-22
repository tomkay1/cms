/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.utils;

import com.huotu.hotcms.service.config.ServiceTestConfig;
import com.huotu.hotcms.service.util.HttpUtils;
import com.huotu.hotcms.service.widget.model.GoodsSearcher;
import com.huotu.huobanplus.common.entity.Product;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwb on 2016/4/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceTestConfig.class)
@WebAppConfiguration
public class HttpUtilsTest {

    @Autowired
    protected MockHttpServletRequest request;
    private MockMvc mockMvc;

    @Test
    public void getRequestParamTest() throws Exception{
        request.setParameter("keyword","dd");
        GoodsSearcher goodsSearcher = HttpUtils.getRequestParam(request, GoodsSearcher.class);
        Assert.assertEquals(goodsSearcher.getKeyword(),"dd");
    }

    @Test
    public void test() throws Exception{
        Product product1 = new Product();
        Product product2 = new Product();
        Product product3 = new Product();
        Product product4 = new Product();
        product1.setId(Long.valueOf(1));
        product2.setId(Long.valueOf(2));
        product3.setId(Long.valueOf(3));
        product4.setId(Long.valueOf(4));
        product1.setSpec("红色,s");
        product2.setSpec("红色,L");
        product3.setSpec("蓝色,s");
        product4.setSpec("蓝色,L");
        List<Product> productList = new ArrayList();
        productList.add(product1);
        productList.add(product2);
        productList.add(product3);
        productList.add(product4);
    }

}
