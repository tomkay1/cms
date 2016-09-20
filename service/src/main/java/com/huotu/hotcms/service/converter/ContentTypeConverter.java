/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.converter;

import com.huotu.hotcms.service.common.ContentType;
import org.springframework.stereotype.Component;

/**
 * @author CJ
 */
@Component
public class ContentTypeConverter extends CommonEnumConverter<ContentType> {
    public ContentTypeConverter() {
        super(ContentType.class);
    }

    @Override
    public ContentType convert(String source) {
        try {
            return super.convert(source);
        } catch (NumberFormatException ex) {
            for (ContentType type : ContentType.values()) {
                if (type.name().equalsIgnoreCase(source))
                    return type;
            }
        }
        return null;
    }
}
