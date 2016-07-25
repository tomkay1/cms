/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.entity;

import com.huotu.hotcms.service.Auditable;
import com.huotu.hotcms.service.Copyable;
import com.huotu.hotcms.service.common.PageType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.widget.page.PageLayout;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;


/**
 * 用与保存页面信息
 * Created by lhx on 2016/7/4.
 */
@Entity
@Table(name = "cms_pageInfo")
@Getter
@Setter
public class PageInfo implements Auditable, Copyable<PageInfo> {

    /**
     * 父级page
     */
    @ManyToOne
    @JoinColumn(name = "parentPageId")
    PageInfo parent;

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
    @Convert(converter = PageLayoutConverter.class)
    @Column(columnDefinition = "text")//略嫌糟糕
    private PageLayout layout;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PageInfo)) return false;
        PageInfo pageInfo = (PageInfo) o;
        return Objects.equals(pageId, pageInfo.pageId) &&
                Objects.equals(pagePath, pageInfo.pagePath) &&
                Objects.equals(title, pageInfo.title) &&
                pageType == pageInfo.pageType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageId, pagePath, title, pageType);
    }

    @Override
    public PageInfo copy() {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setCategory(category);
        pageInfo.setUpdateTime(LocalDateTime.now());
        pageInfo.setCreateTime(LocalDateTime.now());
        pageInfo.setTitle(title);
        pageInfo.setResourceKey(UUID.randomUUID().toString());
//        pageInfo.setPageSetting(pageSetting);
        pageInfo.setLayout(layout);
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
