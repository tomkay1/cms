package com.huotu.hotcms.widget.resolve.thymeleaf;

import com.huotu.hotcms.widget.WidgetResolveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Map;

/**
 * 将w:class转为控件独有的值
 * Created by lhx on 2016/7/8.
 */
@Component
public class ClassProcessor extends AbstractAttributeTagProcessor implements IElementTagProcessor
        , WidgetProcessor {

    @Autowired
    WidgetResolveService widgetResolveService;

    public ClassProcessor() {
        super(TemplateMode.HTML, WidgetDialect.Prefix, null, true, "class", true, 10000, false);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName
            , String attributeValue, IElementTagStructureHandler structureHandler) {

    }

}
