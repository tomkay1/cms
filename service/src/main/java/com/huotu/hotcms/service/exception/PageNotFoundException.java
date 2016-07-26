/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 页面不存在异常，返回404
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PageNotFoundException extends Exception {

    public PageNotFoundException() {
    }

    public PageNotFoundException(String msg){
        super(msg);
    }
}
