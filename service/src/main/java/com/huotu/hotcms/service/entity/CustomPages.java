package com.huotu.hotcms.service.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by Administrator on 2016/3/18.
 */
@Entity
@Table(name = "cms_customPages")
@Getter
@Setter
public class CustomPages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 页面名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 页面描述信息
     * */
    @Column(name = "description")
    private String description;

    /**
     * 是否为首页
     * */
    @Column(name = "isHome")
    private boolean isHome=false;

    /**
    * 是否发布
    * */
    @Column(name ="isPublish")
    private boolean isPublish=false;

    /**
     * 排序权重
     */
    @Column(name = "orderWeight")
    private int orderWeight;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private LocalDateTime createTime;

    /**
     * 是否已删除
     */
    @Column(name = "deleted")
    private boolean deleted = false;

    /**
     * 所属站点
     */
    @ManyToOne
    @JoinColumn(name = "siteId")
    private Site site;
}
