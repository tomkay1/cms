/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.widget.test;

import com.huotu.hotcms.widget.WidgetStyle;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Locale;

/**
 * @author CJ
 */
public class DemoStyle implements WidgetStyle {
    @Override
    public String id() {
        return "look";
    }

    @Override
    public String name() {
        return "Look";
    }

    @Override
    public String name(Locale locale) {
        return "看";
    }

    @Override
    public String description() {
        return "看毛";
    }

    @Override
    public String description(Locale locale) {
        return "看毛";
    }

    @Override
    public Resource thumbnail() {
        return new ClassPathResource("thumbnail.png", getClass().getClassLoader());
    }

    @Override
    public Resource previewTemplate() {
        return null;
    }

    @Override
    public Resource browseTemplate() {
        return new ClassPathResource("look.html", getClass().getClassLoader());
    }
}
