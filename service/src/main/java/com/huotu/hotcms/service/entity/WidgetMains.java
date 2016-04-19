package com.huotu.hotcms.service.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cms_widgetMains")
@Getter
@Setter
public class WidgetMains {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 控件主体名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 控件主体缩略图
     * */
    @Column(name = "imageUri")
    private String imageUri;

    /**
     * 控件主体资源Uri
     * */
    @Column(name ="resourceUri")
    private String resourceUri;

    @Column(name="resourceEditUri")
    private String resourceEditUri;

    /**
     * 控件主体描述信息
     * */
    @Column(name = "description")
    private String description;

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
     * 所属模型
     */
    @ManyToOne
    @JoinColumn(name = "widgetTypeId")
    private WidgetType widgetType;
}
