/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.admin.web;
import com.huotu.hotcms.common.CMSEnums;
import org.springframework.beans.factory.annotation.Autowired;
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
//    @Autowired
//    private HttpServletRequest request;
//
//    @Autowired
//    private HttpServletResponse response;

    public CookieUser(){

    }

    /*
    * Cookie中读取customerID
    * */
    public  int getCustomerId(HttpServletRequest request, int customerId){
        return  CookieHelper.getCookieValInteger(request,CMSEnums.CookieKeyValue.CustomerID.toString());
    }

    /*
    * 获得用户ID
    * */
    public  int getUserId(HttpServletRequest request)
    {
        return  CookieHelper.getCookieValInteger(request,CMSEnums.CookieKeyValue.UserID.toString());
    }

    /*
    * 把商户ID放到Cookie中
    * */
    public void setCustomerId(HttpServletResponse response,int customerId)
    {
        CookieHelper.setCookie(response,CMSEnums.CookieKeyValue.CustomerID.toString(),String.valueOf(customerId),60 * 24 * 7);
    }

    /*
    * 获得角色id,-1为超级管理员
    * */
    public int getRoleId(HttpServletRequest request)
    {
        return  CookieHelper.getCookieValInteger(request,CMSEnums.CookieKeyValue.RoleID.toString());
    }

    /*
    * 检查登录
    * */
    public  boolean checkLogin(HttpServletRequest request,int customerId)
    {
        boolean loginIndex=false;
        String userIdStr=CookieHelper.getCookieVal(request,CMSEnums.CookieKeyValue.UserID.toString());
        if(null!=userIdStr&&userIdStr.equals(""))
        {
            int userId=getUserId(request);
            if(getRoleId(request)==-1)//超级管理员
            {
                loginIndex=true;
            }
            else
            {
                if(userId==customerId||customerId==0)
                    loginIndex=true;
                else
                    loginIndex=false;
            }
        }
        else
            loginIndex=false;
        return  loginIndex;

//        bool loginIndex = false;
//        try
//        {
//            string userIDStr = CookieHelper.GetCookieVal(System.Enum.GetName(typeof(CookieKeyValue), CookieKeyValue.UserID));
//            if (!string.IsNullOrEmpty(userIDStr))
//            {
//                int userID = Convert.ToInt32(CookieHelper.GetCookieVal(System.Enum.GetName(typeof(CookieKeyValue), CookieKeyValue.UserID)));
//                if (CookieUser.instance.GetRoleID() == -1)//超级管理员
//                {
//                    loginIndex = true;
//                }
//                else { //非超级管理员
//                    if (userID == customerid||customerid==0)
//                        loginIndex = true;
//                    else
//                        loginIndex = false;
//                }
//            }
//            else
//                loginIndex = false;
//        }
//        catch
//        {
//            return false;
//        }
//        return loginIndex;
    }
}
