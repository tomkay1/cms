package com.huotu.hotcms.service.entity;

import com.huotu.hotcms.service.common.RouteType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 站点路由规则
 * Created by xhl on 2015/12/21.
 */
@Entity
@Table(name = "cms_route")
@Setter
@Getter
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商户ID
     */
    @Column(name = "customerId")
    private Integer customerId;

    /**
     * 排序权重
     */
    @Column(name = "orderWeight")
    private int orderWeight;

    /**
     * 是否已删除
     */
    @Column(name = "deleted")
    private boolean deleted = false;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "updateTime")
    private LocalDateTime updateTime;
    /**
     * 路由规则,标准正则表达式
     */
    @Column(name = "rule")
    private String rule;

    /**
     * 目标模版地址（相对）
     */
    @Column(name = "template")
    private String template;

    /**
     * 所属站点
     */
    @ManyToOne
    @JoinColumn(name = "siteId")
    private Site site;

    /**
     * 路由描述信息
     * */
    @Column(name = "description")
    private String description;

    @Column(name = "routeType")
    private RouteType routeType;

}
