/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.widget.test.hold;

import com.huotu.hotcms.widget.Widget;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * @author CJ
 */
public class TestWidgetHolder extends AbstractWidgetHolder {
    public TestWidgetHolder() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        super(new ClassPathResource("META-INF/widget.properties")
                , className -> (Widget) Class.forName(className).newInstance());
    }
}
