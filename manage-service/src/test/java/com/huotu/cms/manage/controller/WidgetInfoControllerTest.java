/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.ManageTest;
import com.huotu.cms.manage.page.AdminPage;
import com.huotu.cms.manage.page.WidgetPage;
import com.huotu.hotcms.widget.entity.WidgetInfo;
import com.huotu.hotcms.widget.repository.WidgetInfoRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 后台控件管理的测试
 *
 * @author CJ
 */
public class WidgetInfoControllerTest extends ManageTest {

    @Autowired
    private WidgetInfoRepository widgetInfoRepository;

    @Test
    public void flow() throws Exception {
        AdminPage adminPage = loginAsManage();

        WidgetPage page = adminPage.toWidget();

        // 以不同的方式添加控件
        WidgetInfo widgetInfo1 = randomWidgetInfoValue();
        page = page.addWidgetWithoutOwnerAndJar(widgetInfo1);

        assertThat(widgetInfoRepository.getOne(widgetInfo1.getIdentifier()))
                .isNotNull();
        assertThat(widgetInfoRepository.getOne(widgetInfo1.getIdentifier()).getType())
                .isEqualTo(widgetInfo1.getType());

    }

    private WidgetInfo randomWidgetInfoValue() {
        WidgetInfo info = new WidgetInfo();
        info.setGroupId(randomDomain());
        info.setWidgetId(randomDomain());
        info.setVersion(randomDomain());
        info.setType(randomDomain());
        return info;
    }

}