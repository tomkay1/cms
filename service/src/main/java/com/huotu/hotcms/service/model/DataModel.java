package com.huotu.hotcms.service.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lhx on 2016/8/20.
 */
@Getter
@Setter
public class DataModel {
    private int pageNum;
    private int pageSize;
    private int totalPages;
    private long totalElements;
    private DataObject[] data;
}
