package com.huotu.hotcms.service.config;

import com.huotu.huobanplus.sdk.common.CommonClientSpringConfig;
import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@ComponentScan({"com.huotu.hotcms.service"})
@EnableTransactionManagement
@ImportResource({"classpath:spring_dev.xml","classpath:spring_prod.xml"})
@Import({CommonClientSpringConfig.class})
public class ServiceConfig {


}
