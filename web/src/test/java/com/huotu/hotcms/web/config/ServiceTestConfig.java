package com.huotu.hotcms.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by chendeyu on 2016/4/19.
 */
@Configuration
@ComponentScan({
        "com.huotu.hotcms.web.config",
        "com.huotu.hotcms.service.config"
})
public class ServiceTestConfig {
}