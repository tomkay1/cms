/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @brief Thymeleaf 自定义方言循环参数实体类
 * @since 1.0.0
 * @author xhl
 * @time 2015/12/30
 */
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
