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
 * 列表遍历解析器参数模型(公告、链接、下载)
 * Created by cwb on 2016/1/18.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class NormalForeachParam extends BaseForeachParam {
//
//    /**
//     * 所属栏目id
//     */
//    protected Long categoryid;

    /**
     * 所属二级栏目id(所属栏目的父节点)，与所属栏目id存在性互斥
     */
    @Rename("parentcid")
    public Long parentcId;

    /**
     * 取得列表大小(为了兼容1.0版本,该属性保留)
     */
    @Rename("size")
    public Integer size;

//    /**
//     * 获取列表时排除的主键Id(可排除多个，逗号分隔)
//     */
//    protected String[] excludeids;
//
//    /**
//     * 获取指定Id的列表(可指定多个，逗号分隔)
//     */
//    protected String[] specifyids;
//
//    public String[] getExcludeids() {
//        return excludeids;
//    }
//
//    public void setExcludeids(String[] excludeids) {
//        this.excludeids = excludeids;
//    }
//
//    public String[] getSpecifyids() {
//        return specifyids;
//    }
//
//    public void setSpecifyids(String[] specifyids) {
//        this.specifyids = specifyids;
//    }
//
//    public Integer getSize() {
//        return size;
//    }
//
//    public void setSize(Integer size) {
//        this.size = size;
//    }
//
//    public Long getCategoryid() {
//        return categoryid;
//    }
//
//    public void setCategoryid(Long categoryid) {
//        this.categoryid = categoryid;
//    }
}
