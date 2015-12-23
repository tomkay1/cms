/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 下载模型
 * Created by cwb on 2015/12/22.
 */
@Entity
@Table(name = "cms_download")
@Getter
@Setter
public class Download extends BaseEntity {

    private String fileName;//文件名称
    private String description;//描述信息
    private String downloadUrl;//下载地址
    private int downloads;//下载次数
    @ManyToOne
    private Category category;//所属栏目

}
