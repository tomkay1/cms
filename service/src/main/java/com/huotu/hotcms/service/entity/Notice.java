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

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 公告模型
 * Created by cwb on 2015/12/22.
 */
@Entity
@Table(name = "cms_notice")
@Setter
@Getter
public class Notice extends AbstractContent {

    /**
     * 公告内容
     * 应当是富文本
     */
    @Lob
    private String content;

    @Override
    public Notice copy() {
        Notice notice = new Notice();
        copyTo(notice);
        notice.setContent(content);
        return notice;
    }

}
