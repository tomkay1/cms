package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.common.PageErrorType;
import com.huotu.hotcms.service.service.FailPageService;
import com.huotu.hotcms.service.util.HttpUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Locale;

/**
 * Created by Administrator on 2016/4/11.
 */
@Service
public class FailPageServiceImpl implements FailPageService {
    private TemplateEngine templateEngine;

    @Override
    public String getFailPageTemplate(ApplicationContext applicationContext,PageErrorType pageErrorType) throws IOException {
        InputStream inputStream=((AnnotationConfigWebApplicationContext) applicationContext).getServletContext().getResource(pageErrorType.getValue()).openStream();
        byte[] getData = HttpUtils.readInputStream(inputStream); //获得网站的二进制数据
        String html = new String(getData,"utf-8");
        return html;
    }

//    private String resolveTemplate(String htmlTemplate){
//        Context context=new Context(Locale.CHINA);
//        context.setVariable("site",site);
//        StringWriter writer = new StringWriter();
//        addDialect();
//        templateEngine.process(templateResources,context,writer);
//    }
}
