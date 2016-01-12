/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 下载模型
 * Created by cwb on 2015/12/22.
 */
@Entity
@Table(name = "cms_download")
@Getter
@Setter
public class Download extends BaseEntity {

//    /**
//     * 文件名称
//     */
//    @Column(name = "fileName")
//    private String fileName;

//    /**
//     * 描述信息
//     */
//    @Column(name = "description")
//    private String description;

    /**
     * 下载地址
     */
    @Column(name = "downloadUrl")
    private String downloadUrl;

    /**
     * 下载次数
     */
    @Column(name = "downloads")
    private int downloads;

//    /**
//     * 所属栏目
//     */
//    @ManyToOne
//    @JoinColumn(name = "categoryId")
//    private Category category;

}
