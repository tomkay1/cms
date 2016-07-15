/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.common;

/**
 * Created by Administrator on 2015/12/21.
 */
public class CMSEnums {

    /*
    * 系统模型类型
    * */
    public enum CMSModelOptionType{
        ARTICLE_MODEL,
        ANNOUN_MODEL,
        VIDEO_MODEL,
        GALLERT_MODEL,
        DOWN_MODEL,
        LINK_MODEL
    }

    /*
    *
    * */
    public enum CookieKeyValue
    {
        /*
        * 所有者ID
        * */
        OwnerId,

        /*
        * 角色ID
        * */
        RoleID,

        /*
        * 登录用户名
        * */
        xLoginName,

        /*
        * 用户ID
        * */
        UserID,

        /*
        * 角色名称
        * */
        RoleName,

        /*
        * 登录次数
        * */
        LoginCount,

        /*
        * 上次登录IP
        * */
        LastLoginIP,

        /*
        * 上次登录时间
        * */
        LastLoginTime,

        /*
        * 登陆类型 0：管理员登陆，1：客户登陆
        * */
        LoginType,

        /*
        * 当前登录时间
        * */
        CurrentLoginTime,

        /*
        * 客户行业类型
        * */
        CustomerIndustryType,

        /*
        * 当前登录用户的等级（只只对代理商）
        * */
        CurrentLoginUserLevel,

        /*
        * 当前登录用户所属路径
        * */
        CurrentLoginUserParentPath,

        /*
        * 当前用户版本,0:1.x|1:2.x
        * */
        UserVersion

    }
    public enum MallCookieKeyValue
    {
        /*
        * 商户ID
        * */
        CustomerID,

        /*
        * 用户ID
        * */
        UserId,

        /*
        * 登录用户名
        * */
        xLoginName,

        /*
        * 上次登录地址
         * */
        LoginIP,

        /*
        * 角色名称
        * */
        RoleName,

        /*
        * 登录次数
        * */
        LoginCount,

        /*
        * 上次登录IP
        * */
        LastLoginIP,

        /*
        * 上次登录时间
        * */
        LastLoginTime,


        /*
        * 当前登录时间
        * */
        CurrentLoginTime,

        /*
        * 当前登录用户所属路径
        * */
        CurrentLoginUserParentPath,

        /*
        * 当前用户版本,0:1.x|1:2.x
        * */
        UserVersion

    }


}
