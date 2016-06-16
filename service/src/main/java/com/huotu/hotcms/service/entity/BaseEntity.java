/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据模型基类
 **/
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 序列号
     */
    @Column(length = 100)
    private String serial;
    /**
     * 排序权重
     */
    @Column(name = "orderWeight")
    private int orderWeight;
    /**
     * 是否已删除
     */
    @Column(name = "deleted")
    private boolean deleted;
    /**
     * 标题
     */
    @Column(name = "title", length = 50)
    private String title;
    /**
     * 描述
     */
    @Column(name = "description", length = 200)
    private String description;
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
     * 数据所属栏目
     */
    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    /**
     * 获取数据模型的搜索规格
     *
     * @param ownerId 所有者id必须
     * @param title   可选标题
     * @param deleted 可选是否删除
     * @param <T>     最终类型
     * @return 规格
     */
    public static <T extends BaseEntity> Specification<T> Specification(long ownerId, String title, Boolean deleted) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(title)) {
                predicates.add(cb.like(root.get("title").as(String.class), "%" + title + "%"));
            }
            if (deleted != null)
                predicates.add(cb.equal(root.get("deleted").as(Boolean.class), deleted));

            predicates.add(cb.equal(root.get("category").get("site").get("owner").get("id").as(Long.class), ownerId));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
