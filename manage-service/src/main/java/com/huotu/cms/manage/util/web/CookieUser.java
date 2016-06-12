/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2015. All rights reserved.
 */

package com.huotu.cms.manage.util.web;
import com.huotu.hotcms.service.common.CMSEnums;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录相关Cookie
 * @since 1.0
 * @author xhl
 */
@Component
public class CookieUser {

    public CookieUser(){

    }



    /**
     * * Cookie中读取customerID
       *
     * */
    public  int getCustomerId(HttpServletRequest request){
        return  CookieHelper.getCookieValInteger(request, CMSEnums.CookieKeyValue.CustomerID.toString());
    }

    /**
    * 获得用户ID
    * */
    public  int getUserId(HttpServletRequest request)
    {
        return  CookieHelper.getCookieValInteger(request,CMSEnums.CookieKeyValue.UserID.toString());
    }

    /**
    * 把商户ID放到Cookie中
    * */
    public void setCustomerId(HttpServletRequest request,HttpServletResponse response,int customerId)
    {
        CookieHelper.setCookie(request,response,CMSEnums.CookieKeyValue.CustomerID.toString(),String.valueOf(customerId),1209600);
    }

    /**
    * 获得角色id,-1为超级管理员
    * */
    public int getRoleId(HttpServletRequest request)
    {
        return  CookieHelper.getCookieValInteger(request,CMSEnums.CookieKeyValue.RoleID.toString());
    }

    /**
    * 判断是否是超级管理员
    * */
    public boolean isSupper(HttpServletRequest request)
    {
        return getRoleId(request)==-1;
    }

    /**
     * 判断是否是该商户,一般删除用该方法做商户权限判断,删除操作一般比较严重,所以需要做严格的权限匹配
     * */
    public boolean isCustomer(HttpServletRequest request,Integer customerId){
        return customerId.equals(this.getCustomerId(request));
    }

    /**
    * 检查登录
    * */
    public  boolean checkLogin(HttpServletRequest request,HttpServletResponse response,int customerId)
    {
        if(customerId==-1){
            customerId=Integer.valueOf(CookieHelper.getCookieVal(request,CMSEnums.CookieKeyValue.CustomerID.toString()));
        }
        boolean loginIndex=false;
        String userIdStr=CookieHelper.getCookieVal(request,CMSEnums.CookieKeyValue.UserID.toString());
        if(null!=userIdStr&&!userIdStr.equals(""))
        {
            setCustomerId(request,response,customerId);
            int userId=getUserId(request);
            if(isSupper(request)){//超级管理员
                loginIndex=true;
            }
            else{
                if(userId==customerId||customerId==0)
                    loginIndex=true;
                else
                    loginIndex=false;
            }
        }
        else
            loginIndex=false;
        return  loginIndex;
    }
}
