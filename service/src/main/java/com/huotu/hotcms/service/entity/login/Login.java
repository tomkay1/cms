/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.entity.login;

import com.huotu.hotcms.service.entity.AbstractContent;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Site;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 可登录者
 *
 * @author CJ
 */
public interface Login extends UserDetails {

    String Role_Template_Value = "TEMPLATE";
    /**
     * 可管理模板权限
     */
    String Role_Template = "ROLE_" + Role_Template_Value;

    String Role_Manage_Value = "MANAGE";
    /**
     * 可管理正文权限
     */
    String Role_Manage = "ROLE_" + Role_Manage_Value;

    String Role_AS_Value = "AS";
    /**
     * 可运行以{@link #Role_Manage}权限
     */
    String Role_AS = "ROLE_" + Role_AS_Value;

    String Role_ManageOwner_Value = "OWNER";
    /**
     * 可管理Owner权限
     */
    String Role_ManageOwner = "ROLE_" + Role_ManageOwner_Value;


    /**
     * @param site
     * @return true 表示可以管理这个站点
     */
    boolean siteManageable(Site site);

    /**
     * @param host
     * @return true 表示可以管理这个域名
     */
    boolean hostManageable(Host host);

    /**
     * @param content
     * @return true 表示可以管理这个正文
     */
    boolean contentManageable(AbstractContent content);

    /**
     * @param category
     * @return true 表示可以管理这个栏目
     */
    boolean categoryManageable(Category category);


    /**
     * @return 是超级管理员
     */
    boolean isRoot();

    /**
     * @return 当前管理的ownerId
     */
    Long currentOwnerId();

    /**
     * 更新当前的管理ownerId
     *
     * @param ownerId ownerId
     */
    void updateOwnerId(Long ownerId);

    /**
     * @return 当前管理的站点id
     */
    Long currentSiteId();

    /**
     * 更新当前的管理站点id
     *
     * @param siteId siteId
     */
    void updateSiteId(Long siteId);

    /**
     * 更新密码
     *
     * @param password 密码的密文
     * @see org.springframework.security.crypto.password.PasswordEncoder
     */
    void setPassword(String password);
}
