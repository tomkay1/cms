/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service;

/**
 * 这个玩意儿是有序号的,并且在范畴(比如站点)内唯一
 *
 * @author CJ
 */
public interface Serially {
    /**
     * @return 获取序列号
     */
    String getSerial();

    /**
     * @param serial 新的序列号
     */
    void setSerial(String serial);
}
