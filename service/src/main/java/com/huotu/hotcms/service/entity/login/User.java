/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.entity.login;

import com.huotu.hotcms.service.entity.BaseEntity;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Site;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collection;
import java.util.Collections;

/**
 * cms的用户
 *
 * @author CJ
 */
@Entity
@Table(name = "cms_user")
public class User extends AbstractLogin {


    @Override
    public boolean siteManageable(Site site) {
        return false;
    }

    @Override
    public boolean hostManageable(Host host) {
        return false;
    }

    @Override
    public boolean contentManageable(BaseEntity content) {
        return false;
    }

    @Override
    public boolean categoryManageable(Category category) {
        return false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptySet();
    }
}
