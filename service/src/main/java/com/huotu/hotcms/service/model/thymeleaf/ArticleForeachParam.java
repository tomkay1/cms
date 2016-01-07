/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.model.thymeleaf;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by cwb on 2016/1/6.
 */
@Getter
@Setter
public class ArticleForeachParam {

    /**
     * 所属栏目Id
     */
    private String categoryid;

    /**
     * 获取列表时排除的主键Id
     */
    private String excludeid;

    /**
     * 页码
     */
    private String pageno;

    /**
     * 列表大小
     */
    private String pagesize;

}
