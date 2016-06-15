/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service.impl;

import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.exception.FormatException;
import com.huotu.hotcms.widget.service.WidgetFactoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by elvis on 2016/6/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@WebAppConfiguration
@Transactional
public class TestWidgetFactoryServiceImpl {

    @Autowired
    private WidgetFactoryService widgetFactoryService;

    @Test
    public void testInstallWidget() throws IOException, FormatException {

        String randomType = UUID.randomUUID().toString();
        // 安装一个demo控件
        widgetFactoryService.installWidget("com.huotu.wedget", "picBanner", "1.0-SNAPSHOT", randomType);

        // 校验列表,应当包含picBanner控件
        assertWidgetListContainWidgetName("picBanner", randomType);

        widgetFactoryService.reloadWidgets();

        assertWidgetListContainWidgetName("picBanner", randomType);
    }

    private void assertWidgetListContainWidgetName(String widgetName, String type) throws IOException, FormatException {
        Widget newWidget = null;
        for (InstalledWidget widget : widgetFactoryService.widgetList()) {
            if (type.equals(widget.getType())) {
                newWidget = widget.getWidget();
                break;
            }
        }

        assertThat(newWidget)
                .isNotNull();
        assertThat(newWidget.name())
                .isEqualToIgnoringCase(widgetName);
    }

//    @Test
//    public void testGetInstallWidget() {
//
//        try {
//            Assert.assertNotNull(widgetFactoryService.widgetList());
//        } catch (FormatException e) {
//            Assertions.fail(e.toString());
//        } catch (IOException e) {
//            Assertions.fail(e.toString());
//        }
//    }


}
