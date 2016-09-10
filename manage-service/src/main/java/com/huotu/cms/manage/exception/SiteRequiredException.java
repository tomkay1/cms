/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.exception;

/**
 * 执行操作时我们可能需要用户先选择好一个站点,简单的理解就是我们要「用到」站点,但我们没法替用户决定。
 *
 * @author CJ
 */
public class SiteRequiredException extends RuntimeException {
}
