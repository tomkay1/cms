package com.huotu.hotcms.service.config;

import com.huotu.huobanplus.sdk.common.CommonClientSpringConfig;
import com.huotu.huobanplus.sdk.mall.MinMallSDKConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@ComponentScan({"com.huotu.hotcms.service"})
@EnableTransactionManagement
@ImportResource({"classpath:spring_prod.xml"})
@Import({CommonClientSpringConfig.class,MinMallSDKConfig.class})
public class ServiceConfig {


}
