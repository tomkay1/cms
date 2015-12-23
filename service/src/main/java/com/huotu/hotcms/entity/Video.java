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
 * 视频模型
 * Created by cwb on 2015/12/22.
 */
@Entity
@Table(name = "cms_video")
@Getter
@Setter
public class Video extends BaseEntity {

    private String title;//视频名称
    private String thumbUri;//缩略图uri
    private String description;//描述信息
    private String videoUrl;//内部储存地址
    private String outLinkUrl;//外部链接地址
    private int palyTimes;//播放次数
    @ManyToOne
    private Category category;//所属栏目

}
