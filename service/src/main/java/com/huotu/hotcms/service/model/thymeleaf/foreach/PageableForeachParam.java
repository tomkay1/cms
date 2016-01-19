/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.model.thymeleaf.foreach;

/**
 * 分页遍历解析器参数模型(文章、视频)
 * Created by cwb on 2016/1/18.
 */
public class PageableForeachParam {

    /**
     * 所属栏目id
     */
    protected Long categoryid;
    /**
     * 所属二级栏目id(所属栏目的父节点)，与所属栏目id存在性互斥
     */
    private Long parentcid;
    /**
     * 页码
     */
    private Integer pageno;
    /**
     * 分页大小
     */
    private Integer pagesize;
    /**
     * 指定需要展示的页数
     */
    private Integer pagenumber;
    /**
     * 获取列表时排除的主键Id(可排除多个，逗号分隔)
     */
    protected String[] excludeids;
    /**
     * 获取指定Id的列表(可指定多个，逗号分隔)
     */
    protected String[] specifyids;

    public Long getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(Long categoryid) {
        this.categoryid = categoryid;
    }

    public String[] getExcludeids() {
        return excludeids;
    }

    public void setExcludeids(String[] excludeids) {
        this.excludeids = excludeids;
    }

    public String[] getSpecifyids() {
        return specifyids;
    }

    public void setSpecifyids(String[] specifyids) {
        this.specifyids = specifyids;
    }

    public Long getParentcid() {
        return parentcid;
    }

    public void setParentcid(Long parentcid) {
        this.parentcid = parentcid;
    }

    public Integer getPageno() {
        return pageno;
    }

    public void setPageno(Integer pageno) {
        this.pageno = pageno;
    }

    public Integer getPagesize() {
        return pagesize;
    }

    public void setPagesize(Integer pagesize) {
        this.pagesize = pagesize;
    }

    public Integer getPagenumber() {
        return pagenumber;
    }

    public void setPagenumber(Integer pagenumber) {
        this.pagenumber = pagenumber;
    }
}
