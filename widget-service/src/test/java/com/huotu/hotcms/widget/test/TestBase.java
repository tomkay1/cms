package com.huotu.hotcms.widget.test;

import com.huotu.hotcms.widget.config.TestConfig;
import com.huotu.hotcms.widget.page.Page;
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
    protected final Logger logger= LoggerFactory.getLogger(TestBase.class);

    @Autowired
    private WebApplicationContext webApplication;

    protected MockMvc mockMvc;

    @Before
    public final void initMockMvc(){
        mockMvc= MockMvcBuilders.webAppContextSetup(webApplication).build();
    }

    /**
     * 生成随机的测试{@link com.huotu.hotcms.widget.page.Page}数据
     * @return
     */
    protected Page randomPage(){
        Page page=new Page();
        page.setPageIdentity(UUID.randomUUID().toString());
        page.setTitle(UUID.randomUUID().toString());
        return page;
    }
}
