/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.interceptor;

import com.huotu.hotcms.service.exception.PageNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionInterceptor {
    private static final Log log = LogFactory.getLog(ExceptionInterceptor.class);

    @ExceptionHandler(PageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void pageNotFound(PageNotFoundException e) {
        log.warn("Page Not Found :" + e.getLocalizedMessage(), e);
    }
}
