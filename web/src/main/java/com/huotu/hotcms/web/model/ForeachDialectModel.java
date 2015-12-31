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

    /*
    * 过滤ID,多个用逗号隔开
    * */
    private String ignoreId;

    /*
    * 显示数量,0或者null为全部
    * */
    private Integer size;

//    /*
//    * 数据源类型
//    * */
//    private String dataSources;

}
