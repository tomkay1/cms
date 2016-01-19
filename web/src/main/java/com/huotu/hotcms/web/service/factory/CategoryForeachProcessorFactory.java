/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.web.service.factory;

import com.huotu.hotcms.service.common.RouteType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.thymeleaf.foreach.CategoryForeachParam;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.web.thymeleaf.expression.DialectAttributeFactory;
import com.huotu.hotcms.web.thymeleaf.expression.VariableExpression;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;

/**
 * Created by cwb on 2016/1/6.
 */
public class CategoryForeachProcessorFactory {

    private final int DEFAULT_SIZE = 6;

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
        try {
            WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
            CategoryForeachParam categoryForeachParam = DialectAttributeFactory.getInstance().getForeachParam(elementTag, CategoryForeachParam.class);
            CategoryService categoryService = (CategoryService)applicationContext.getBean("categoryServiceImpl");
            //根据指定id获取栏目列表
            if(!StringUtils.isEmpty(categoryForeachParam.getSpecifyids())) {
                return categoryService.getSpecifyCategories(categoryForeachParam.getSpecifyids());
            }
            //设置路由类型
            Route route = (Route)VariableExpression.getVariable(context,"route");
            if(categoryForeachParam.getRoutetype()==null) {
                categoryForeachParam.setRoutetype(route.getRouteType());
            }
            //设置站点id
            if (StringUtils.isEmpty(categoryForeachParam.getSiteid())) {
                Site site = (Site) VariableExpression.getVariable(context, "site");
                categoryForeachParam.setSiteid(site.getSiteId());
            }
            if(isHeaderCategory(categoryForeachParam.getRoutetype())) {
                return categoryService.getHeaderCategoryList(categoryForeachParam);
            }
            if(categoryForeachParam.getSize()==null) {
                categoryForeachParam.setSize(DEFAULT_SIZE);
            }else if(categoryForeachParam.getSize()<1) {
                categoryForeachParam.setSize(1);
            }
            //根据所属父节点及路由类型取得列表
            //设置父节点id
            Long parentId = categoryForeachParam.getParentid();
            if(parentId==null) {
                Category current = categoryService.getCategoryByRoute(route);
                categoryForeachParam.setParentid(current.getId());
            }
            return categoryService.findByRouteTypeAndParentId(categoryForeachParam);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private boolean isHeaderCategory(RouteType routetype) {
        return routetype==RouteType.HEADER_NAVIGATION;
    }
}
