/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 父模型
 **/
@Getter
@Setter
public class BaseModel {

    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 商户ID
     */
    private Integer customerId;

    /**
     * ownerId
     */
    private long ownerId;

    /**
     * 排序权重
     */
    private int orderWeight;

    /**
     * 是否已删除
     */
    private boolean deleted = false;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
