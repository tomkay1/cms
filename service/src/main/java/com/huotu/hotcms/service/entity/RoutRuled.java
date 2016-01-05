package com.huotu.hotcms.service.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 站点路由规则
 * Created by xhl on 2015/12/21.
 */
@Entity
@Table(name = "cms_routRuled")
@Setter
@Getter
public class RoutRuled  extends BaseEntity {


    /**
     * 路由规则,标准正则表达式
     */
    @Column(name = "rule")
    private String rule;

    /*
    * 目标模版地址（相对）
    * */
    @Column(name = "template")
    private String template;

    /**
     * 所属站点
     */
    @ManyToOne
    @JoinColumn(name = "siteId")
    private Site site;
}
