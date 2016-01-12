package com.huotu.hotcms.service.model;

import com.huotu.hotcms.service.common.ModelType;
import com.huotu.hotcms.service.entity.BaseEntity;
import com.huotu.hotcms.service.entity.Site;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by chendeyu on 2016/1/4.
 */
@Setter
@Getter
public class CategorySite extends BaseModel {

    /**
     * 栏目名称
     */
    private String name;

    /**
     * 父级栏目
     */
    private CategorySite parent;

    /**
     * 父级编号
     */
    private Long parentId;

    /**
     * 是否自定义模型
     */
    private boolean custom;

    /**
     * 系统数据类型
     */
    private ModelType modelType;

    /**
     * 所属站点
     */
    private Long siteId;

    /**
     * 所属站点名称
     */
    private String siteName;

    private Site site;


    /**
     * 定义calss
     */
    private String categoryClass;
}
