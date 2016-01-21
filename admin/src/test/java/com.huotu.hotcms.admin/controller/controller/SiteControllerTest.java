package com.huotu.hotcms.admin.controller.controller;

import com.huotu.hotcms.admin.controller.config.AdminTestConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by chendeyu on 2016/1/20.
 */

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AdminTestConfig.class,})
@WebAppConfiguration
@Transactional
public class SiteControllerTest {

    private static final Log log = LogFactory.getLog(SiteControllerTest.class);


}
