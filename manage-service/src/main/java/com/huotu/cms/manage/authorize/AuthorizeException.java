/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.authorize;

import com.huotu.cms.manage.authorize.annoation.AuthorizeRole;

/**
 * @author CJ
 */
public class AuthorizeException extends Exception {

    public AuthorizeException(AuthorizeRole.Role required) {

    }

    public AuthorizeException(AuthorizeRole.Role required, String message) {

    }

    public AuthorizeException(AuthorizeRole.Role required, String message, Throwable throwable) {

    }

}
