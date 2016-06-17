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
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 可登录者
 *
 * @author CJ
 */
public interface Login extends UserDetails {

    /**
     * 可管理正文权限
     */
    String Role_Manage = "MANAGE";
    /**
     * 可运行以{@link #Role_Manage}权限
     */
    String Role_ManageAS = "AS";
    /**
     * 可管理Owner权限
     */
    String Role_ManageOwner = "OWNER";

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
    boolean contentManageable(BaseEntity content);

    /**
     * @param category
     * @return true 表示可以管理这个栏目
     */
    boolean categoryManageable(Category category);


}
