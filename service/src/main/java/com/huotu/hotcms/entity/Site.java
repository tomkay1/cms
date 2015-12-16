package com.huotu.hotcms.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Administrator on 2015/12/16.
 */
@Entity
@Table(name = "cms_site")
@Setter
@Getter
public class Site {

    @Id
    private Long siteId;
    private String name;	// 站点名称
    private String title;	// 站点标题
    private String logo;	// 站点logo
    private String description;// 描述，填写有助于搜索引擎优化
    private String keywords;// 关键字，填写有助于搜索引擎优化
    private String theme;	// 主题
    private String copyright;// 版权信息
    private String customIndexView;// 自定义首页视图文件
    private String domain;

}
