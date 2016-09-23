/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.model;

import com.huotu.hotcms.service.entity.Site;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 站点和序列号,按照我们的规范,它可以绝对定位一个资源
 *
 * @author CJ
 */
@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class SiteAndSerial {

    private Site site;
    private String serial;

    @Override
    public String toString() {
        return serial + "@" + site.getSiteId();
    }
}
