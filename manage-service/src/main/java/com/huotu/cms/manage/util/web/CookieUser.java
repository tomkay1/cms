/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.util.web;

import com.huotu.hotcms.service.common.CMSEnums;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录相关Cookie
 *
 * @author xhl
 * @since 1.0
 */
@Service
public class CookieUser {

    public CookieUser() {

    }


    /**
     * 从cookie中获取当前用户customerId
     *
     * @param request 请求
     * @return customerId 0 就是没有登录
     */
    public int getCustomerId(HttpServletRequest request) {
        return CookieHelper.getCookieValInteger(request, CMSEnums.MallCookieKeyValue.CustomerID.toString());
    }

    /**
     * * Cookie中读取ownerId
     */
    public long getOwnerId(HttpServletRequest request) {
        return CookieHelper.getCookieValInteger(request, CMSEnums.CookieKeyValue.OwnerId.toString());
    }

    /**
     * 获得用户ID
     */
    public int getUserId(HttpServletRequest request) {
        return CookieHelper.getCookieValInteger(request, CMSEnums.CookieKeyValue.UserID.toString());
    }

    /**
     * 把商户ID放到Cookie中
     */
    public void setOwnerId(HttpServletRequest request, HttpServletResponse response, long owner) {
        CookieHelper.setCookie(request, response, CMSEnums.CookieKeyValue.OwnerId.toString(), String.valueOf(owner)
                , 1209600);
    }

    /**
     * 获得角色id,-1为超级管理员
     */
    public int getRoleId(HttpServletRequest request) {
        return CookieHelper.getCookieValInteger(request, CMSEnums.CookieKeyValue.RoleID.toString());
    }

    /**
     * 判断是否是超级管理员
     */
    public boolean isSupper(HttpServletRequest request) {
        return getRoleId(request) == -1;
    }

    /**
     * 判断是否是该商户,一般删除用该方法做商户权限判断,删除操作一般比较严重,所以需要做严格的权限匹配
     */
    public boolean isOwnerLogin(HttpServletRequest request, long ownerId) {
        return ownerId == (this.getOwnerId(request));
    }

    /**
     * 检查登录
     */
    public boolean checkLogin(HttpServletRequest request, HttpServletResponse response, long ownerId) {
        if (ownerId == -1) {
            String cookieValue = CookieHelper.getCookieVal(request, CMSEnums.CookieKeyValue.OwnerId.toString());
            if (cookieValue == null)
                return false;
            ownerId = Integer.valueOf(cookieValue);
        }
        boolean loginIndex;
        String userIdStr = CookieHelper.getCookieVal(request, CMSEnums.CookieKeyValue.UserID.toString());
        if (null != userIdStr && !userIdStr.equals("")) {
            setOwnerId(request, response, ownerId);
            int userId = getUserId(request);
            //超级管理员
            loginIndex = isSupper(request) || userId == ownerId || ownerId == 0;
        } else
            loginIndex = false;
        return loginIndex;
    }
}
