package com.huotu.hotcms.service.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 父模型
 * **/
@Getter
@Setter
public class BaseModel {

    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 商户ID
     */
    private Integer customerId;

    /**
     * 排序权重
     */
    private int orderWeight;

    /**
     * 是否已删除
     */
    private boolean deleted = false;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
