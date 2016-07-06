/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.entity;

import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.widget.entity.support.WidgetIdentifier;
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

@Entity
@Table(name = "cms_widgetInfo")
@Getter
@Setter
@IdClass(WidgetIdentifier.class)
public class WidgetInfo {

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
    @Column(name = "widgetId", length = 50)
    private String widgetId;
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

    public WidgetIdentifier getIdentifier() {
        return new WidgetIdentifier(groupId, widgetId, version);
    }


}
