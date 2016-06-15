/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.widget.test;

import com.huotu.hotcms.widget.Widget;
import com.huotu.widget.test.bean.WidgetHolder;
import me.jiangcai.lib.test.SpringWebTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
@ContextConfiguration(classes = WidgetTestConfig.class)
@WebAppConfiguration
public abstract class WidgetTest extends SpringWebTest {

    /**
     * 正在测试的目标控件
     */
    @SuppressWarnings("WeakerAccess")
    protected Widget widget;

    @Autowired
    public void setWidgetHolder(WidgetHolder holder) {
        widget = holder.getWidget();
    }

    /**
     * 一些常用属性测试
     *
     * @throws IOException
     */
    @Test
    public void properties() throws IOException {
//        assertThat(widget.name())
//                .isNotEmpty();

//        assertThat(widget.author())
//                .isNotEmpty();

        assertThat(widget.description())
                .isNotEmpty();

        assertThat(widget.groupId())
                .isNotEmpty();

        assertThat(widget.widgetId())
                .isNotEmpty();

//        assertThat(widget.version())
//                .isNotEmpty();

        if (widget.publicResources() != null) {
            widget.publicResources().forEach((name, resource) -> {
                assertThat(name)
                        .as("公开资源名字不可为空")
                        .isNotNull();
                assertThat(resource)
                        .as("公开资源内容不可为空")
                        .isNotNull();
                assertThat(resource.isReadable())
                        .as("公开资源需可读")
                        .isTrue();
                assertThat(resource.exists())
                        .as("公开资源必须存在")
                        .isTrue();
            });
        }

        assertThat(widget.thumbnail())
                .isNotNull();
        assertThat(widget.thumbnail().isReadable())
                .as("必须拥有缩略图")
                .isTrue();
        //图片资源必须是 106x82 png
        BufferedImage thumbnail = ImageIO.read(widget.thumbnail().getInputStream());
        assertThat((float) thumbnail.getWidth() / (float) thumbnail.getHeight())
                .as("缩略图必须为106*82的PNG图片")
                .isEqualTo(106f / 82f);

        // 现在检查编辑器
    }
}
