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
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 栏目
 * Created by cwb on 2015/12/21.
 */
@Entity
@Table(name = "cms_category")
@Setter
@Getter
public class Category {

    @Id
    private Long categoryId;
    private Integer customerId;
    private String name;//栏目名称
    private Category parent;//父级栏目
    private String parentIds; // 所有父级编号，用逗号分隔
    private int orderWeight;//排序权重
    private boolean deleted = false;//是否已删除
    private DataModel dataModel;//数据类型
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
