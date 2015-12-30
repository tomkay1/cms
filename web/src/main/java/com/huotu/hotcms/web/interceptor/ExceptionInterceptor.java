/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.web.interceptor;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by Administrator on 2015/12/18.
 */
@ControllerAdvice
public class ExceptionInterceptor {


    @ExceptionHandler(Throwable.class)
    public String catchExceptions(Throwable e) {
        e.printStackTrace();
        return "/view/error.html";
    }

}
