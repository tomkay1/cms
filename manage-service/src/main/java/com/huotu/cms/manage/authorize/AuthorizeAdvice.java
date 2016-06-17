/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.authorize;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author CJ
 */
@ControllerAdvice
public class AuthorizeAdvice {

    /**
     * @param ex
     */
    @ExceptionHandler(AuthorizeException.class)
    public void sawAuthorizeException(AuthorizeException ex, @RequestHeader("X-Requested-With") String ajax) {
        // TODO
    }

}
