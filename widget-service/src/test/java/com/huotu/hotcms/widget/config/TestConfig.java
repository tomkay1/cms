package com.huotu.hotcms.widget.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by wenqi on 2016/5/31.
 */
@Configuration
@ComponentScan(value = {"com.huotu.hotcms.admin.config"
        })
public class TestConfig {
}
