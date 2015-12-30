package com.huotu.hotcms.service.config;

import org.springframework.context.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;


@Configuration
@ComponentScan({"com.huotu.hotcms.service.service"})
@EnableTransactionManagement
@ImportResource({"classpath:spring.xml"})
public class ServiceConfig {


    /**
     * 可以替换为其他Password
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
