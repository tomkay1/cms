/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.templateresource;

import com.huotu.hotcms.service.widget.service.PageResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.util.Validate;

import java.io.*;

/**
 * 组件模板资源
 * Created by cwb on 2016/3/16.
 */
public class WidgetTemplateResource implements ITemplateResource {

    @Autowired
    private PageResourceService pageResourceService;

//  private final Resource resource;
    private final String characterEncoding;

    public WidgetTemplateResource(final ApplicationContext applicationContext, final String location, final String characterEncoding) {
        super();
        Validate.notNull(applicationContext, "Application Context cannot be null");
        Validate.notEmpty(location, "Resource Location cannot be null or empty");
//        SiteResolveService siteResolveService = (SiteResolveService) applicationContext.getBean("siteResolveService");
//        Site site = siteResolveService.getCurrentSite(request1);
        // Character encoding CAN be null (system default will be used)
//        this.resource = applicationContext.getResource(location);
        this.characterEncoding = characterEncoding;
    }

    public WidgetTemplateResource(final Resource resource,final String characterEncoding) {

        super();

        Validate.notNull(resource, "Resource cannot be null");
        // Character encoding CAN be null (system default will be used)

//        this.resource = resource;
        this.characterEncoding = characterEncoding;
    }

    @Override
    public String getDescription() {
//        return this.resource.getDescription();
        return "";
    }

    @Override
    public String getBaseName() {
//        return computeBaseName(this.resource.getFilename());
        return "";
    }

    @Override
    public boolean exists() {
//        return this.resource.exists();
        return true;
    }

    @Override
    public Reader reader() throws IOException {
        String htmlTemplate="";
//       String htmlTemplate=pageResourceService.getHtmlTemplateByWidgetPage();
//        String test="<div th:text=\"${test2}\" style=\"color:red\"></div><div>测试</div><div></div>";
        //TODO 自定义流
//        final InputStream inputStream = this.resource.getInputStream();
//        final InputStream inputStream = is;
        return new StringReader(htmlTemplate);
//        return new BufferedReader(new InputStreamReader(new BufferedInputStream(inputStream)));
    }

    @Override
    public ITemplateResource relative(final String relativeLocation) throws IOException {
//        return new WidgetTemplateResource(this.resource.createRelative(relativeLocation), this.characterEncoding);
        return null;
    }

    static String computeBaseName(final String path) {
        if (path == null) {
            return null;
        }

        // First remove a trailing '/' if it exists
        final String basePath = (path.charAt(path.length() - 1) == '/'? path.substring(0,path.length() - 1) : path);

        final int slashPos = basePath.lastIndexOf('/');
        if (slashPos != -1) {
            final int dotPos = basePath.lastIndexOf('.');
            if (dotPos != -1 && dotPos > slashPos + 1) {
                return basePath.substring(slashPos + 1, dotPos);
            }
            return basePath.substring(slashPos + 1);
        }

        return basePath;

    }
}
