/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.service;

import com.huotu.hotcms.service.thymeleaf.common.DialectTypeEnum;
import com.huotu.hotcms.service.thymeleaf.service.factory.TimeFormatProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;

/**
 * <p>
 *     时间处理方言入口
 * </p>
 *
 * @author xhl
 *
 * @since 1.0.0
 */
@Component
public class FormatProcessorService {

    @Autowired
    private TimeFormatProcessorService timeFormatProcessorService;

    public Object resolveDataByAttr(String dialectPrefix, IProcessableElementTag tab, ITemplateContext context, Object expressionResult) {
        if (dialectPrefix.equals(DialectTypeEnum.TIME.getDialectPrefix())) {
            return timeFormatProcessorService.resolveDataByAttr(tab, context, expressionResult);
        }
        return null;
    }
}
