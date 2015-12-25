package com.huotu.hotcms.util;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 2015/12/25.
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
}
