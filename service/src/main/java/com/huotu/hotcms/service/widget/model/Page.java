/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.widget.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 自定义分页
 * Created by cwb on 2016/3/17.
 */
@Getter
@Setter
public class Page {

    /**
     * 当前页码
     */
    private Integer number;
    /**
     * 总页数
     */
    private Integer totalPages;
    /**
     * 总记录数
     */
    private Integer totalElements;
    /**
     * 结果集
     */
    private Integer size;

}
