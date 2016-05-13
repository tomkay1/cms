package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.config.ServiceTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by fawzi on 2016/5/12.
 * 用于site相关测试
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceTestConfig.class)
@WebAppConfiguration
@Transactional
public class SiteTest {
    @Autowired SiteService siteService;
    /**
     * 站点复制测试
     */
    @Test
    public void testSiteCopy(){
        siteService.siteCopy(null,null);
    }
}
