/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.entity;

import com.huotu.hotcms.service.Auditable;
import com.huotu.hotcms.service.Enabled;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.entity.support.WidgetIdentifier;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "cms_widgetInfo")
@Getter
@Setter
@IdClass(WidgetIdentifier.class)
public class WidgetInfo implements Enabled, Auditable {

    /**
     * 禁用还是做不到,但可以让商户看不到
     */
    private boolean enabled;
    /**
     * 站点创建时间
     */
    @Column(name = "createTime")
    private LocalDateTime createTime;
    @Id
    @Column(name = "groupId", length = 50)
    private String groupId;
    @Id
    @Column(name = "artifactId", length = 50)
    private String artifactId;
    @Id
    @Column(name = "version", length = 50)
    private String version;

    @Column(name = "type", length = 100)
    private String type;
    @ManyToOne
    @JoinColumn(name = "ownerId")
    private Owner owner;
    /**
     * 数据包在资源系统中的路径
     *
     * @see me.jiangcai.lib.resource.service.ResourceService
     */
    @Column(length = 100)
    private String path;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WidgetInfo)) return false;
        WidgetInfo that = (WidgetInfo) o;
        return Objects.equals(groupId, that.groupId) &&
                Objects.equals(artifactId, that.artifactId) &&
                Objects.equals(version, that.version) &&
                Objects.equals(type, that.type) &&
                Objects.equals(owner, that.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, artifactId, version, type, owner);
    }

    public WidgetIdentifier getIdentifier() {
        return new WidgetIdentifier(groupId, artifactId, version);
    }

    @Override
    public LocalDateTime getUpdateTime() {
        return null;
    }

    @Override
    public void setUpdateTime(LocalDateTime updateTime) {

    }
}
