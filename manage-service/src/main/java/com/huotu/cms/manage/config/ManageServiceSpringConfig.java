/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.config;

import me.jiangcai.lib.embedweb.EmbedWeb;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 载入manage-service的Spring配置类
 *
 * @author CJ
 */
@Configuration
@EnableWebSecurity
@Order(99)//毕竟不是老大 100就让给别人了
@ComponentScan("com.huotu.cms.manage")
public class ManageServiceSpringConfig extends WebSecurityConfigurerAdapter implements EmbedWeb {

    @Override
    public String name() {
        return "manage-service";
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
        http
                .authorizeRequests().antMatchers(
                "/manage/**"
        ).authenticated()
                .and()
                .formLogin()
                .loginPage("/manage/main/login")
                .permitAll();
    }
}
