package com.huotu.hotcms.web.thymeleaf.dialect;

import com.huotu.hotcms.web.service.BaseDialectService;
import com.huotu.hotcms.web.thymeleaf.processor.TextProcessor;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <p>
 *     自定义内容 thymeleaf 语法标签类
 * </p>
 * @since 1.0.0
 *
 * @author xhl
 */
public class TextDialect extends AbstractProcessorDialect {
    public static final String NAME = "huotu";
//    public static final String PREFIX = "hot";//前缀
    public static final int PROCESSOR_PRECEDENCE = 800;
    public static  String ATTR_NAME = "text";//属性
    private static BaseDialectService baseDialectService;


    public TextDialect(String name, String prefix, String attrName, BaseDialectService dialectService){
        super(name,prefix,PROCESSOR_PRECEDENCE);
        this.baseDialectService=dialectService;
        this.ATTR_NAME=attrName;
    }


    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        return createHotProcessorsSet(this,dialectPrefix);
    }

    public static Set<IProcessor> createHotProcessorsSet(final IProcessorDialect dialect, final String dialectPrefix) {
        Set<IProcessor> processors = new LinkedHashSet<>();
        processors.add(new TextProcessor(dialect, TemplateMode.HTML, dialectPrefix,ATTR_NAME,baseDialectService));
        return processors;
    }
//    @Override
//    public Set<IProcessor> getProcessors(String dialectPrefix) {
//        return null;
//    }
}
