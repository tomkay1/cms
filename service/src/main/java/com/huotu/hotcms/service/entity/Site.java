package com.huotu.hotcms.service.entity;

import com.huotu.hotcms.service.common.SiteType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 站点
 * Created by cwb on 2015/12/21.
 */
@Entity
@Table(name = "cms_site")
@Setter
@Getter
@Cacheable(value = false)
public class Site implements Cloneable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "siteId")
    private Long siteId;

    /**
     * 商户ID
     */
    @Column(name = "customerId")
    private Integer customerId;

    /**
     * 站点名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 标题，填写有助于搜索引擎优化
     */
    @Column(name = "title")
    private String title;

    /**
     * 关键字，填写有助于搜索引擎优化
     */
    @Column(name = "keywords")
    private String keywords;

    /**
     * 描述，填写有助于搜索引擎优化
     */
    @Column(name = "description")
    private String description;

    /**
     * 站点logo
     */
    @Column(name = "logoUri")
    private String logoUri;

    /**
     * 版权信息
     */
    @Column(name = "copyright")
    @Lob
    private String copyright;

    /**
     * 是否自定义模板
     */
    @Column(name = "custom")
    private boolean custom = false;

    /**
     * 自定义模板根路径
     */
    @Column(name = "customViewUrl")
    private String customTemplateUrl;

    /**
     * 站点是否个性化
     * */
    @Column(name = "personalise")
    private boolean personalise;

     /**
      * 对应域名
      */
     @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
     @JoinTable(name = "cms_site_host",
     joinColumns = {@JoinColumn(name = "siteId",referencedColumnName = "siteId")},
     inverseJoinColumns = {@JoinColumn(name = "hostId",referencedColumnName = "hostId")}
     )
     private Set<Host> hosts;

     /**
     * 站点创建时间
     */
    @Column(name = "createTime")
    private LocalDateTime createTime;

    /**
     * 站点更新时间
     */
    @Column(name = "updateTime")
    private LocalDateTime updateTime;

    /**
     * 是否已删除
     */
    @Column(name = "deleted")
    private boolean deleted = false;

    /**
     * 资源根路径(可以CDN方式读取缓存)
     */
    @Column(name = "resourceUrl")
    private String resourceUrl;

    /**
     * 网站类型(pc 商城or pc shop)
     * */
    @Column(name = "siteType")
    private SiteType siteType;
    /**
     * 所属地区
     */
    @OneToOne(optional = false)
    @JoinColumn(name = "regionId")
    private Region region;

    public void addHost(Host host) {
        if(this.hosts == null) {
            this.hosts = new HashSet<>();
        }
        if(host.getSites() == null) {
            host.setSites(new HashSet<>());
        }
        if(this.getSiteId()!=null) {
            host.getSites().add(this);
        }
        this.hosts.add(host);
    }

    public void removeHost(Host host) {
        host.getSites().remove(this);
        this.hosts.remove(host);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
