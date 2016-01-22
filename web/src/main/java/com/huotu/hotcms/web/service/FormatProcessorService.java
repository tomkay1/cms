package com.huotu.hotcms.web.service;

import com.huotu.hotcms.web.common.DialectTypeEnum;
import com.huotu.hotcms.web.service.factory.TimeFormatProcessorService;
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
public class FormatProcessorService extends BaseProcessorService{
    public Object resolveDataByAttr(IProcessableElementTag tab,ITemplateContext context,Object expressionResult){
        if (dialectPrefix.equals(DialectTypeEnum.TIME.getDialectPrefix())) {
            return TimeFormatProcessorService.getInstance().resolveDataByAttr(tab,context,expressionResult);
        }
        return null;
    }
}
