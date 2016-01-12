package com.huotu.hotcms.service.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 数据模型基类
 * **/
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
public class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 描述信息
     */
    @Column(name = "description")
    private String description;

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
     * 所属栏目
     */
    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;
}
