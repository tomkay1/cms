/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.entity;

import com.huotu.hotcms.service.Auditable;
import com.huotu.hotcms.service.Copyable;
import com.huotu.hotcms.service.common.PageType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;


/**
 * 用与保存页面信息
 * Created by lhx on 2016/7/4.
 */
@Entity
@Table(name = "cms_pageInfo")
@Getter
@Setter
public class PageInfo implements Auditable,Copyable<PageInfo> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pageId")
    private Long pageId;

    @Column(name = "pagePath", length = 60)
    private String pagePath;

    @Column(name = "title", length = 60)
    private String title;

    /**
     * 可选的数据源
     */
    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @ManyToOne(optional = false)
    @JoinColumn(name = "siteId")
    private Site site;

    @Column(name = "createTime")
    private LocalDateTime createTime;

    @Column(name = "updateTime")
    private LocalDateTime updateTime;

    @Column(name = "pageType", nullable = false)
    private PageType pageType;

    /**
     * 每次随机生成
     */
    @Column(name = "resourceKey", length = 60)
    private String resourceKey;

    /**
     * 页面配置的xml数据
     */
    @Lob
    @Column(name = "pageSetting")
    private byte[] pageSetting;

    @Override
    public PageInfo copy() {
        PageInfo pageInfo=new PageInfo();
        pageInfo.setCategory(category);
        pageInfo.setUpdateTime(LocalDateTime.now());
        pageInfo.setCreateTime(LocalDateTime.now());
        pageInfo.setTitle(title);
        pageInfo.setResourceKey(UUID.randomUUID().toString());
        pageInfo.setPageSetting(pageSetting);
        pageInfo.setPagePath(pagePath);
        pageInfo.setPageType(pageType);
        pageInfo.setSite(site);
        return pageInfo;
    }

    @Override
    public PageInfo copy(Site site, Category category) {
        return copy();
    }
}
