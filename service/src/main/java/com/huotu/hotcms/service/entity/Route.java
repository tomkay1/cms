package com.huotu.hotcms.service.entity;

import com.huotu.hotcms.service.common.RouteType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 站点路由规则
 * Created by xhl on 2015/12/21.
 */
@Entity
@Table(name = "cms_route")
@Setter
@Getter
public class Route extends BaseEntity {
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
