package com.huotu.hotcms.service.entity;

/**
 * Created by hzbc on 2016/5/18.
 */

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 模板类型
 */

@Entity
@Getter
@Setter
@Table(name = "cms_template_type")
public class TemplateType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * 行业
     */
    @Column(name = "industry")
    private String industry;

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
}
