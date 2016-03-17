package com.huotu.hotcms.service.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cms_widgetType")
@Getter
@Setter
public class WidgetType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 控件主体类型名称
     */
    @Column(name = "name")
    private String name;

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
}
