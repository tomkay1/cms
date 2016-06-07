package com.huotu.hotcms.widget.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by wenqi on 2016/6/1.
 */

@Configuration
@ComponentScan("com.huotu.hotcms.widget")
@ImportResource({"classpath:spring_dev.xml","classpath:spring_prod.xml"})
public class WidgetConfig {
}
