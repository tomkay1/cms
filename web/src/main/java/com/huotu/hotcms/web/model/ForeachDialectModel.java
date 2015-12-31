package com.huotu.hotcms.web.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * @brief Thymeleaf 自定义方言循环参数实体类
 * @since 1.0.0
 * @author xhl
 * @time 2015/12/30
 */
@Entity
@Getter
@Setter
public class ForeachDialectModel extends BaseDialectModel {

    private String ignoreId;

    private Integer size;

}
