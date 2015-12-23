package com.huotu.hotcms.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 站点
 * Created by cwb on 2015/12/21.
 */
@Entity
@Table(name = "cms_site")
@Setter
@Getter
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long siteId;//主键
    private Integer customerId;	// 商户ID
    private String name;//站点名称
    private String title;	// 站点标题
    private String description;// 描述，填写有助于搜索引擎优化
    private String keywords;// 关键字，填写有助于搜索引擎优化
    private String logoUri;	// 站点logo
    private String copyright;// 版权信息
    private boolean custom = false;//是否自定义视图
    private String customViewUrl;//自定义视图根路径
    private String hosts;//可以配置多域名，用逗号（","）隔开
    private LocalDateTime createTime;//站点创建时间
    private LocalDateTime updateTime;//站点更新时间

}
