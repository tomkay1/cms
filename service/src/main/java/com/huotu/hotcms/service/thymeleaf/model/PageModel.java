/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.model;

/**
 * Created by cwb on 2016/1/13.
 */

public class PageModel {

    /**
     * 页码
     */
    private int pageNo;

    /**
     * 页码链接
     */
    private String pageHref;



    public String getPageHref() {
        return pageHref;
    }

    public void setPageHref(String pageHref) {
        this.pageHref = pageHref;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
}
