package com.huotu.hotcms.web.service.factory;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.web.service.BaseProcessorService;
import com.huotu.hotcms.web.thymeleaf.expression.VariableExpression;
import com.huotu.hotcms.web.util.PatternMatchUtil;
import com.huotu.hotcms.web.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.expression.IExpressionObjects;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator xhl 2016/1/6.
 */
public class SiteCurrentProcessorFactory  extends BaseProcessorService {
    private static final String regexp="\\$\\{([^\\}]+)}";//匹配${key}模式的正则表达式

    private static final Log log = LogFactory.getLog(CategoryForeachProcessorFactory.class);

    private static SiteCurrentProcessorFactory instance;

    private SiteCurrentProcessorFactory() {
    }

    public static SiteCurrentProcessorFactory getInstance() {
        if(instance == null) {
            instance = new SiteCurrentProcessorFactory();
        }
        return instance;
    }

    @Override
    public Object resolveDataByAttr(String attributeValue, ITemplateContext context){
        Site site = (Site) VariableExpression.getVariable(context, "site");
        try {
            String attributeName= PatternMatchUtil.getMatchVal(attributeValue, regexp);
            attributeName= StringUtil.toUpperCase(attributeName);
            Object object = site.getClass().getMethod("get"+attributeName).invoke(site);
            return object;
        }
        catch (Exception ex){
            log.error(ex.getMessage());
        }
        return null;
    }
}
