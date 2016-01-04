package com.huotu.hotcms.web.service;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.expression.IExpressionObjects;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 自定义thymeleaf 语法标签解析
 * </P>
 *
 * @author xhl
 * @since 1.0.0
 */
public class ArticleCurrentProcessorService extends BaseProcessorService{

    @Override
    public Object resolveDataByAttr(String attributeValue, ITemplateContext context) {
        IExpressionObjects expressContent= context.getExpressionObjects();
        HttpServletRequest request=(HttpServletRequest)expressContent.getObject("request");
//        String path=request.getContextPath();
////        /content/(\d*).html
//        String regexp="/content/(\\d*).html";
//        Pattern pattern = Pattern.compile(regexp);
//        Matcher match = pattern.matcher(request.getRequestURI());
//        if(match.matches())
//        {
//            return match.group(0);
//        }
        return "测试";
//        return super.resolveDataByAttr(attributeValue, context);
    }
}
