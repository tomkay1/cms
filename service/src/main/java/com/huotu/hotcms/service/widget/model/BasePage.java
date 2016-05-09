package com.huotu.hotcms.service.widget.model;

import lombok.Data;

/**
 * Created by Administrator on 2016/4/28.
 */
@Data
public class BasePage {
    /**
     * 页码
     */
    public int pageNo;

    /**
     * 总页码记录数
     */
    public int totalPages;

    /**
     * 总记录数
     */
    public long totalRecords;

//    /**
//     * 分页显示页码数量
//     */
//    public int pageBtnCount;
}
