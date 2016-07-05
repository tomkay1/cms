/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.entity;

import com.huotu.hotcms.service.entity.Site;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by hzbc on 2016/6/27.
 */

/**
 * 页面信息
 */
@Entity
@Setter
@Getter
public class PageInfo {

    /**
     * 页面ID
     */
    @Id
    private String pageId;

    /**
     * 站点ID
     */
    //@OneToMany
    @Column(name = "siteId")
    private long siteId;

    /**
     * 页面配置的xml数据
     * @see  com.huotu.hotcms.widget.page.Page
     */
    @Lob
    private byte[] pageSetting;
}
