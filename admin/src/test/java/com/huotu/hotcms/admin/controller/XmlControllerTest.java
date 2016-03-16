package com.huotu.hotcms.admin.controller;

import com.huotu.hotcms.admin.config.TestAdminConfig;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

/**
 * Created by Administrator on 2016/3/15.
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestAdminConfig.class)
@WebAppConfiguration
@Transactional
public class XmlControllerTest extends WebTestBase  {

}

