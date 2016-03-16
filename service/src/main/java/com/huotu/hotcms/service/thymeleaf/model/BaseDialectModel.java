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
 * @brief Thymeleaf 方言参数基类模型
 * @since 1.0.0
 * @author xhl
 * @time 2015/12/30
 */
@Getter
@Setter
public abstract class BaseDialectModel {
    private Long Id;

    private Long siteId;
}
