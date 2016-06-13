/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service.impl;

import com.huotu.hotcms.widget.exception.FormatException;
import com.huotu.hotcms.widget.service.WidgetFactoryService;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * Created by elvis on 2016/6/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Configuration
@ContextConfiguration(classes = TestConfig.class)
@WebAppConfiguration
@Transactional
public class TestWidgetFactoryServiceImpl {

    @Autowired
    private WidgetFactoryService widgetFactoryService;

    @Test
    public void testInstallWidget() {
        try {
            widgetFactoryService.installWidget("commons-dbcp","commons-dbcp","1.4","type1");
        } catch (FormatException e) {
            Assertions.fail(e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetInstallWidget() {

        try {
            Assert.assertNotNull(widgetFactoryService.widgetList());
        } catch (FormatException e) {
            Assertions.fail(e.toString());
        } catch (IOException e) {
            Assertions.fail(e.toString());
        }
    }


}
