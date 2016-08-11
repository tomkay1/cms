/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.model.thymeleaf.foreach;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 图库参数模型
 * </p>
 *
 * @author xhl
 * @since 1.2
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GalleryForeachParam extends BaseForeachParam {
    /**
     * 所属图库的id
     */
    @Rename("galleryid")
    public Long galleryId;
}
