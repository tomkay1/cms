/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.config;

import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.widget.config.WidgetConfig;
import me.jiangcai.lib.embedweb.EmbedWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.annotation.PostConstruct;

/**
 * 载入manage-service的Spring配置类
 *
 * @author CJ
 */
@Configuration
@EnableWebSecurity
@Order(99)//毕竟不是老大 100就让给别人了
@ComponentScan({"com.huotu.cms.manage"})
@Import(WidgetConfig.class)
public class ManageServiceSpringConfig extends WebSecurityConfigurerAdapter implements EmbedWeb {

    @Autowired
    private Environment environment;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public String name() {
        return "manage-service";
    }

    @PostConstruct
    public void init() {
        applicationContext.getBeansOfType(SpringTemplateEngine.class)
                .values().forEach(engine -> engine.addDialect(new SpringSecurityDialect()));
    }
//
//    @SuppressWarnings("SpringJavaAutowiringInspection")
//    @Autowired
//    public void setTemplateEngineSet(Set<SpringTemplateEngine> templateEngineSet) {
//        // 所有都增加安全方言
//        templateEngineSet.forEach(engine -> engine.addDialect(new SpringSecurityDialect()));
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
//        .authorizeRequests().antMatchers("/**").hasRole("USER").antMatchers("/admin/**")
//                .hasRole("ADMIN")

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();

        // 在测试环境下 随意上传
        if (environment.acceptsProfiles("test") || environment.acceptsProfiles("development")) {
            registry = registry
                    .antMatchers("/manage/upload").permitAll()
                    .antMatchers("/manage/upload/fine").permitAll();
        }
        registry
                .antMatchers("/manage/**").hasRole(Login.Role_Manage_Value)
                .antMatchers("/manage/supper/**").hasRole("ROOT")
                .and()
                .csrf().disable()
                .formLogin()
                .loginPage("/manage/main/login")
                .permitAll()
                .and()
                .logout().logoutUrl("/logout").permitAll();
    }
}
