/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.exception;

/**
 * 组件属性异常,特指该组件属性无法被控件所接受
 * 这个异常应该是用于测试属性是否可用
 *
 * @author CJ
 */
public class ComponentPropertiesException extends Exception {
    public ComponentPropertiesException() {
    }

    public ComponentPropertiesException(String message) {
        super(message);
    }

    public ComponentPropertiesException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComponentPropertiesException(Throwable cause) {
        super(cause);
    }

    public ComponentPropertiesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
