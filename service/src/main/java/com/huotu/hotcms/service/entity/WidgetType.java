package com.huotu.hotcms.service.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "cms_widgetType")
@Getter
@Setter
@Cacheable(value = false)
public class WidgetType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "typeId")
    private Long typeId;

    /**
     * 站点名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 排序权重
     */
    @Column(name = "orderWeight")
    private int orderWeight;
}
