/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;

/**
 * api json 返回模型
 * @since 1.0.0
 * @author xhl
 */
public class PageData<T> {

    private int PageSize;

    private  int PageIndex;

    private long Total;

    private int PageCount;

    private T[] Rows;

    @JsonProperty(value = "PageSize")
    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int pageSize) {
        PageSize = pageSize;
    }

    @JsonProperty(value = "PageIndex")
    public int getPageIndex() {
        return PageIndex;
    }

    public void setPageIndex(int pageIndex) {
        PageIndex = pageIndex;
    }

    @JsonProperty(value = "Total")
    public long getTotal() {
        return Total;
    }

    public void setTotal(long total) {
        Total = total;
    }

    @JsonProperty(value = "PageCount")
    public int getPageCount() {
        return PageCount;
    }

    public void setPageCount(int pageCount) {
        PageCount = pageCount;
    }

    @JsonProperty(value = "Rows")
    public T[] getRows() {
        return Rows;
    }

    public void setRows(T[] rows) {
        Rows = rows;
    }

    /*
    * Page<T> 转换成PageData<T>对象
    * */
    public  PageData<T> ConvertPageData(Page<T> pageData, T[] newList) {
        PageData<T> data=null;
        if (pageData != null) {
            data = new PageData<T>();
            data.setPageCount(pageData.getTotalPages());
            data.setPageIndex(pageData.getNumber());
            data.setPageSize(pageData.getSize());
            data.setTotal(pageData.getTotalElements());
            data.setRows(pageData.getContent().toArray(newList));
        }
        return  data;
    }
}
