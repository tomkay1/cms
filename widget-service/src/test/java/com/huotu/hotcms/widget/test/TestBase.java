/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.test;

import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.config.TestConfig;
import com.huotu.hotcms.widget.page.Layout;
import com.huotu.hotcms.widget.page.Page;
import com.huotu.hotcms.widget.page.PageElement;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by wenqi on 2016/5/31.
 */

/**
 * CMS单元测试基类
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@WebAppConfiguration
public class TestBase {
    protected final Random random = new Random();
    protected final Logger logger = LoggerFactory.getLogger(TestBase.class);
    protected MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplication;

    @Before
    public final void initMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplication).build();
    }

    /**
     * 生成随机的测试{@link com.huotu.hotcms.widget.page.Page}数据
     *
     * @return
     */
    protected Page randomPage() {
        Page page = new Page();
        page.setPageIdentity(UUID.randomUUID().toString());
        page.setTitle(UUID.randomUUID().toString());

        List<PageElement> pageElementList = new ArrayList<>();
        int layouts = random.nextInt(10) + 1;
        while (layouts-- > 0) {
            pageElementList.add(randomLayout());
        }

        int comps = random.nextInt(5) + 1;
        while (comps-- > 0) {
            pageElementList.add(randomComponent());
        }

        page.setElements(pageElementList.toArray(new PageElement[pageElementList.size()]));

        return page;
    }

    private Component randomComponent() {
        return null;
    }

    private Layout randomLayout() {
        Layout layout = new Layout();

        List<PageElement> pageElementList = new ArrayList<>();
        int layouts = random.nextInt(2);
        while (layouts-- > 0) {
            pageElementList.add(randomLayout());
        }

        int comps = random.nextInt(2);
        while (comps-- > 0) {
            pageElementList.add(randomComponent());
        }

        layout.setElements(pageElementList.toArray(new PageElement[pageElementList.size()]));

        return layout;
    }
}
