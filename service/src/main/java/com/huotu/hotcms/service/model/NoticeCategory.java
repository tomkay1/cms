package com.huotu.hotcms.service.model;

import com.huotu.hotcms.service.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by chendeyu on 2016/1/5.
 */
@Setter
@Getter
public class NoticeCategory extends BaseModel {

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 所属栏目
     */
    private String categoryName;
}
