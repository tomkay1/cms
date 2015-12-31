package com.huotu.hotcms.web.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * @brief Thymeleaf 方言参数基类模型
 * @since 1.0.0
 * @author xhl
 * @time 2015/12/30
 */
@Entity
@Getter
@Setter
public abstract class BaseDialectModel {
    private Long Id;

    private Long siteId;
}
