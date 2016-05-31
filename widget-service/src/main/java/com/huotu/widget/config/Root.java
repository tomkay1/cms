package com.huotu.widget.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by hzbc on 2016/5/31.
 */
@Configuration
@Import(value = {MvcConfig.class})
@ComponentScan(value = "com.huotu.widget")
public class Root {

}
