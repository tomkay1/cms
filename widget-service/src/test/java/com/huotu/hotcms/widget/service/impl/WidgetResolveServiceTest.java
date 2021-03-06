/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service.impl;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetResolveService;
import com.huotu.hotcms.widget.WidgetStyle;
import com.huotu.hotcms.widget.test.TestBase;
import me.jiangcai.lib.resource.service.ResourceService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 测试widget的重绘
 *
 * @author CJ
 */
public class WidgetResolveServiceTest extends TestBase {

    @Autowired
    private WidgetResolveService widgetResolveService;
    @Autowired(required = false)
    private HttpServletResponse response;

    @Test
    @Transactional
    public void css() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        Site site = randomSite(randomOwner());
        widgetResolveService.widgetDependencyContent(CMSContext.PutContext(request, response, site)
                , new CSSWidget(), Widget.CSS, null, buffer);

        StreamUtils.copy(buffer.toByteArray(), System.out);

        byte[] expected = StreamUtils.copyToByteArray(new ClassPathResource("css/result.css").getInputStream());
        if (buffer.toByteArray().length < expected.length) {
            byte[] newOne = new byte[buffer.toByteArray().length];
            System.arraycopy(expected, 0, newOne, 0, newOne.length);
            expected = newOne;
        }
        assertThat(buffer.toByteArray())
                .containsExactly(expected);
    }

    class CSSWidget implements Widget {

        @Override
        public String groupId() {
            return "a";
        }

        @Override
        public String widgetId() {
            return "b";
        }

        @Override
        public String name(Locale locale) {
            return "name";
        }

        @Override
        public String description(Locale locale) {
            return "des";
        }

        @Override
        public String dependVersion() {
            return "1.0-SNAPSHOT";
        }

        @Override
        public Map<String, Resource> publicResources() {
            return null;
        }

        @Override
        public Resource widgetDependencyContent(MediaType mediaType) {
            if (mediaType.isCompatibleWith(CSS))
                return new ClassPathResource("css/template.css");
            return null;
        }

        @Override
        public WidgetStyle[] styles() {
            return new WidgetStyle[]{
                    new WidgetStyle() {
                        @Override
                        public String id() {
                            return null;
                        }

                        @Override
                        public String name(Locale locale) {
                            return null;
                        }

                        @Override
                        public String description(Locale locale) {
                            return null;
                        }

                        @Override
                        public Resource thumbnail() {
                            return null;
                        }

                        @Override
                        public Resource previewTemplate() {
                            return null;
                        }

                        @Override
                        public Resource browseTemplate() {
                            return null;
                        }
                    }
            };
        }

        @Override
        public void valid(String styleId, ComponentProperties properties) throws IllegalArgumentException {

        }

        @Override
        public Class springConfigClass() {
            return null;
        }

        @Override
        public ComponentProperties defaultProperties(ResourceService resourceService) throws IOException {
            ComponentProperties properties = new ComponentProperties();
            properties.put("color", "#101010");
            // ansi 没问题
            // # 会加入 \#
            // 数字会 加入 \3
            return properties;
        }
    }

}
