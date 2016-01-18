package com.huotu.hotcms.service.config;

import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@ComponentScan({"com.huotu.hotcms.service.service"})
@EnableTransactionManagement
@ImportResource({"classpath:spring_dev.xml","classpath:spring_prod.xml"})
public class ServiceConfig {


}
