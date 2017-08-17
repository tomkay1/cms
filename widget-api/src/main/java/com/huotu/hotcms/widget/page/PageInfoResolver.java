/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.page;

import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.service.PageService;
import me.jiangcai.lib.resource.service.ResourceService;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author CJ
 */
@Component
public class PageInfoResolver implements HandlerMethodReturnValueHandler {

    private final Template htmlTemplate;
    private final Template responsiveHtmlTemplate;
    private final Template traditionalHtmlTemplate;
    @Autowired
    private PageService pageService;
    @Autowired
    private ResourceService resourceService;

    public PageInfoResolver() throws IOException {
        try (InputStream propertiesFile = new ClassPathResource("/front/velocity.properties").getInputStream()) {
            Properties properties = new Properties();
            properties.load(propertiesFile);
            Velocity.init(properties);
        }

        htmlTemplate = Velocity.getTemplate("/front/html.vm");
        responsiveHtmlTemplate = Velocity.getTemplate("/front/responsive_html.vm");
        traditionalHtmlTemplate = Velocity.getTemplate("/front/traditional_html.vm");
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getParameterType() == PageInfo.class;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer
            , NativeWebRequest webRequest) throws Exception {
        mavContainer.setRequestHandled(true);

        HttpServletResponse response = ((HttpServletResponse) webRequest.getNativeResponse());
        if (returnValue == null) {
            response.setStatus(404);
            return;
        }
        PageInfo pageInfo = (PageInfo) returnValue;
        CMSContext cmsContext = CMSContext.RequestContext();

        String content = pageService.generateHTML(pageInfo, cmsContext);

        VelocityContext context = new VelocityContext();

        context.put("keywords", pageInfo.getSite().getKeywords());
        context.put("description", pageInfo.getSite().getDescription());
        context.put("title", pageInfo.getTitle());
        context.put("globalCssURI", "/css/index.css");
        context.put("pageCssURI", resourceService.getResource(pageInfo.getPageCssResourcePath(false)).httpUrl());
        context.put("content", content);

        response.setContentType("text/html;charset=utf-8");
        if (pageInfo.getPageLayoutType() == null) {
            htmlTemplate.merge(context, response.getWriter());
        } else
            switch (pageInfo.getPageLayoutType()) {
                case traditional:
                    traditionalHtmlTemplate.merge(context, response.getWriter());
                    break;
                case responsive:
                    responsiveHtmlTemplate.merge(context, response.getWriter());
                    break;
                default:
                    htmlTemplate.merge(context, response.getWriter());
            }

        response.getWriter().flush();
    }
}
