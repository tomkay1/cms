/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.exception;

import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Category;
import lombok.Getter;

/**
 * 输入错误的数据源信息
 *
 * @author CJ
 */
@Getter
public class BadCategoryInfoException extends Exception {

    private final Category exceptedParent;
    private final Category category;
    private final ContentType exceptedType;

    public BadCategoryInfoException(Category category, Category exceptedParent) {
        this.category = category;
        this.exceptedParent = exceptedParent;
        this.exceptedType = null;
    }

    public BadCategoryInfoException(Category category, ContentType exceptedType) {
        this.category = category;
        this.exceptedType = exceptedType;
        this.exceptedParent = null;
    }

}
