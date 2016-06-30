/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.login;

import com.huotu.hotcms.service.entity.AbstractContent;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.AbstractLogin;
import org.luffy.libs.libseext.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author CJ
 */
public class Manager extends AbstractLogin implements Serializable {

    private static final long serialVersionUID = 5408870778058706106L;

    private Long ownerId;
    private Long siteId;

    @Override
    public boolean siteManageable(Site site) {
        return true;
    }

    @Override
    public boolean hostManageable(Host host) {
        return true;
    }

    @Override
    public boolean contentManageable(AbstractContent content) {
        return true;
    }

    @Override
    public boolean categoryManageable(Category category) {
        return true;
    }

    @Override
    public boolean isRoot() {
        return true;
    }

    @Override
    public void updateOwnerId(Long ownerId) {
        this.ownerId = ownerId;
        if (this.ownerId == null)
            updateSiteId(null);
    }

    @Override
    public Long currentSiteId() {
        return siteId;
    }

    @Override
    public void updateSiteId(Long siteId) {
        this.siteId = siteId;
    }

    @Override
    public Long currentOwnerId() {
        return ownerId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Collections.singleton(new SimpleGrantedAuthority(Role_Manage));
        return CollectionUtils.mutliSet(
                new SimpleGrantedAuthority("ROLE_ROOT")
                , new SimpleGrantedAuthority(Role_Manage)
                , new SimpleGrantedAuthority(Role_AS)
                , new SimpleGrantedAuthority(Role_ManageOwner)
        );
    }
}
