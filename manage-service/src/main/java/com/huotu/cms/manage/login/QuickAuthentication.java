/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.login;

import com.huotu.hotcms.service.entity.login.Login;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author CJ
 */
public class QuickAuthentication implements Authentication {

    private final Login platformUser;

    public QuickAuthentication(Login platformUser) {
        this.platformUser = platformUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return platformUser.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return platformUser;
    }

    @Override
    public Object getPrincipal() {
        return platformUser;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
    }

    @Override
    public String getName() {
        return platformUser.getUsername();
    }
}
