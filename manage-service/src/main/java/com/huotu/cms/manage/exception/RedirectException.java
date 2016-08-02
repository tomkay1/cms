/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.exception;

import lombok.Getter;

/**
 * 携带着uri的异常,该异常发生时需要转发到指定的视图
 *
 * @author CJ
 */
@Getter
public class RedirectException extends RuntimeException {

    private final String uri;

    public RedirectException(String uri, String message) {
        super(message);
        this.uri = uri;
    }

    public RedirectException(String uri, Throwable cause) {
        super(cause.getLocalizedMessage(), cause);
        this.uri = uri;
    }

    public RedirectException(String uri, String message, Throwable cause) {
        super(message, cause);
        this.uri = uri;
    }

    /**
     * @return 视图名
     */
    public String redirectViewName() {
        return "redirect:" + uri;
    }
}
