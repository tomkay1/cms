/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Locale;

/**
 * 地区/国家
 *
 * Created by cwb on 2015/12/24.
 */
@Entity
@Table(name = "cms_region")
@Getter
@Setter
public class Region {

    @Id
    @Convert(converter = LocaleConverter.class)
    @Column(name = "id", length = 20)
    private Locale locale;

    /**
     * 地区编号（cn,us,etc.）
     */
    @Column(name = "regionCode", length = 5)
    private String regionCode;

    /**
     * 地区名称
     */
    @Column(name = "regionName", length = 100)
    private String regionName;

    /**
     * 语言编号（zh,en,etc.）
     */
    @Column(name = "langCode", length = 5)
    private String langCode;

    /**
     * 语言名称
     */
    @Column(name = "langName", length = 100)
    private String langName;

    /**
     * 语言-地区代码(zh-cn,zh-tw,en-us,etc.)
     */
    @Column(name = "langTag")
    private String langTag;


}
