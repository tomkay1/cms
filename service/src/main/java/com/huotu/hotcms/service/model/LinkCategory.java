package com.huotu.hotcms.service.model;

import com.huotu.hotcms.service.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;


/**
 * Created by chendeyu on 2016/1/5.
 */
@Setter
@Getter
public class LinkCategory extends BaseModel {

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 缩略图uri
     */
    private String thumbUri;

    /**
     * 链接地址
     */
    private String linkUrl;

    /**
     * 所属栏目
     */
    private String categoryName;
}
