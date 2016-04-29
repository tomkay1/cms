package com.huotu.hotcms.service.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * <p>
 *     站点配置信息
 * </p>
 *
 * @since 1.2
 *
 * @author xhl
 */
@Entity
@Table(name = "cms_siteConfig")
@Setter
@Getter
public class SiteConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 商户ID，用于数据冗余,方便后期根据商户来做统计等查询
     */
    @Column(name = "customerId")
    private Integer customerId;

    /**
     * 是否启用微官网功能
     */
    @Column(name = "enabledMobileSite")
    private Boolean enabledMobileSite;

    /**
     * 微官网域名,目前只设置一个域名
     */
    @Column(name = "mobileDomain")
    private String mobileDomain;

    /**
     * 所属站点信息
     */
    @OneToOne
    @JoinColumn(name = "siteId")
    private Site site;
}
