/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.model.thymeleaf.foreach;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 列表遍历解析器参数模型(公告、链接、下载) 1.0 版本
 * Created by cwb on 2016/1/18.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Deprecated
public class NormalForeachParam extends BaseForeachParam {
    /**
     * 所属二级栏目id(所属栏目的父节点)，与所属栏目id存在性互斥
     */
    @Rename("parentcid")
    public Long parentcId;

    /**
     * 取得列表大小(为了兼容1.0版本,
     * 该属性保留,1.0以后版本该属性同基类的pageSize相同,但是为了兼容故该属性优先级高于pageSize)
     */
    @Rename("size")
    @Deprecated
    public Integer size;
}
