/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.web.service.factory;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.thymeleaf.CategoryForeachParam;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.web.service.SiteResolveService;
import com.huotu.hotcms.web.thymeleaf.expression.DialectAttributeFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.expression.IExpressionObjects;
import org.thymeleaf.model.IProcessableElementTag;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwb on 2016/1/6.
 */
public class CategoryForeachProcessorFactory {

    private static final Log log = LogFactory.getLog(CategoryForeachProcessorFactory.class);

    private static CategoryForeachProcessorFactory instance;

    private CategoryForeachProcessorFactory(){
    }

    public static CategoryForeachProcessorFactory getInstance() {
        if(instance == null) {
            instance = new CategoryForeachProcessorFactory();
        }
        return instance;
    }

    public Object process(IProcessableElementTag elementTag, ITemplateContext context) {
        List<Category> categoryList = new ArrayList<>();
        try {
            IExpressionObjects expressContent= context.getExpressionObjects();
            HttpServletRequest request = (HttpServletRequest)expressContent.getObject("request");
            WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
            CategoryForeachParam categoryForeachParam = DialectAttributeFactory.getInstance().getForeachParam(elementTag, CategoryForeachParam.class);
            if(StringUtils.isEmpty(categoryForeachParam.getSiteId())) {
                SiteResolveService siteResolveService = (SiteResolveService)applicationContext.getBean("siteResolveService");
                Site site = null;
                if(siteResolveService.isRootPath(request)) {
                    site = siteResolveService.getHomeSite(request);
                }else if(siteResolveService.isSubSitePath(request)) {
                    site = siteResolveService.getSubSite(request);
                }
                categoryForeachParam.setSiteId(site.getSiteId().toString());
            }
            CategoryService categoryService = (CategoryService)applicationContext.getBean("categoryServiceImpl");
            categoryList = categoryService.getCategoryList(categoryForeachParam);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return categoryList;
    }
}
