/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.config;

import com.huotu.cms.manage.login.Manager;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.repository.OwnerRepository;
import com.huotu.hotcms.widget.config.WidgetConfig;
import me.jiangcai.lib.embedweb.EmbedWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 载入manage-service的Spring配置类
 *
 * @author CJ
 */
@Configuration
@ComponentScan({"com.huotu.cms.manage"})
@Import(WidgetConfig.class)
public class ManageServiceSpringConfig implements EmbedWeb {

    public static final String BuildIn_ROOT = "root";
    public static final String BuildIn_Password = "root_root";

    @Autowired
    private Environment environment;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private OwnerRepository ownerRepository;

    @Override
    public String name() {
        return "manage-service";
    }

    @Autowired
    public void registerSharedAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(username -> {
            // 在测试区域 支持登录管理员
            if (BuildIn_ROOT.equals(username) && environment.acceptsProfiles("test")) {
                Manager manager = new Manager();
                manager.setPassword(passwordEncoder.encode(BuildIn_Password));
                return manager;
            }
            return ownerRepository.findByLoginName(username);
        });
        auth.authenticationProvider(provider);
    }

    @EnableWebSecurity
    @Order(99)//毕竟不是老大 100就让给别人了
    public static class Security extends WebSecurityConfigurerAdapter {

        @Autowired
        private Environment environment;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // 在测试环境下 随意上传
            ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry =
                    http.antMatcher("/manage/**")
                            .authorizeRequests();
            if (environment.acceptsProfiles("test") || environment.acceptsProfiles("development")) {//测试阶段或者开发阶段
                registry = registry
//                    .anyRequest().permitAll()//不妨这样  不要这样! 安全也是业务的一部分 同样需要测试,此处许可仅仅是为了原型测试。
                        .antMatchers("/manage/upload").permitAll()
                        .antMatchers("/manage/upload/fine").permitAll()
                        .antMatchers("/manage/widget/widgets").permitAll()
                        .antMatchers("/manage/owners").permitAll();
            }
            registry
                    .antMatchers("/manage/**").hasRole(Login.Role_Manage_Value)
                    .antMatchers("/manage/supper/**").hasRole("ROOT")
// 更多权限控制
                    .and().csrf().disable()
                    .formLogin()
//                .failureHandler()
                    .loginProcessingUrl("/manage/auth")
                    .loginPage("/manage/main/login")
                    .permitAll()
                    .and()
                    .logout().logoutUrl("/manage/logout").permitAll();
        }
    }

}
