/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.entity;

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


/**
 * Created by lhx on 2016/7/4.
 */
@Entity
@Table(name = "cms_pageInfo")
@Getter
@Setter
public class PageInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pageId;

    @Column(name = "pagePath", unique = true, length = 60)
    private String pagePath;

    @Column(name = "title", length = 60)
    private String title;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @Column(name = "createTime")
    private LocalDateTime createTime;

    @Column
    private PageType pageType;
    /**
     * 页面配置的xml数据
     */
    @Lob
    private byte[] pageSetting;

}
