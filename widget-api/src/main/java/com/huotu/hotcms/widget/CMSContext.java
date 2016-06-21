/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget;

import lombok.Data;

/**
 * CMS上下文,或者称之为交互空间
 * <p>
 * 每一个CMS线程都应当有拥有一个交互空间
 * </p>
 *
 * @author CJ
 */
@Data
public class CMSContext {

    /**
     * CMS站点所有者Id,必选
     */
    private long ownerId;
    /**
     * 商户id,可选
     */
    private Long merchantId;


}
