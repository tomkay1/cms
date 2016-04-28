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
 * 分页遍历解析器参数模型(文章、视频、图片)
 * Created by cwb on 2016/1/18.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PageableForeachParam extends BaseForeachParam{

    /**
     * 所属二级栏目id(所属栏目的父节点)，与所属栏目id存在性互斥
     */
    @Rename("parentcid")
    public Long parentcId;
}
