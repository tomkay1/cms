/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.entity.login;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Objects;

/**
 * 可登录者
 *
 * @author CJ
 */
@Entity
@Table(name = "cms_login", uniqueConstraints = {@UniqueConstraint(columnNames = {"loginName"})})
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class AbstractLogin implements Login {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, unique = true)
    private String loginName;
    @Column(length = 100)
    private String password;
    private boolean enabled = true;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractLogin)) return false;
        AbstractLogin that = (AbstractLogin) o;
        return enabled == that.enabled &&
                Objects.equals(id, that.id) &&
                Objects.equals(loginName, that.loginName) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, loginName, password, enabled);
    }

    @Override
    public String getUsername() {
        return getLoginName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public void updateOwnerId(Long ownerId) {
        throw new NoSuchMethodError("");
    }

    @Override
    public void updateSiteId(Long siteId) {
        throw new NoSuchMethodError("");
    }
}
