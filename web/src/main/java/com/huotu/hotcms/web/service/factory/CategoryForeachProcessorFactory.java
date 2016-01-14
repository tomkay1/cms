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
import com.huotu.hotcms.service.model.thymeleaf.CategoryForeachParam;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.web.service.SiteResolveService;
import com.huotu.hotcms.web.thymeleaf.expression.DialectAttributeFactory;
import com.huotu.hotcms.web.thymeleaf.expression.VariableExpression;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.expression.IExpressionObjects;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.spring4.expression.SPELVariableExpressionEvaluator;
import org.thymeleaf.standard.expression.StandardExpressionExecutionContext;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwb on 2016/1/6.
 */
public class CategoryForeachProcessorFactory {

    private final int DEFAULT_SIZE = 5;

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
            if(categoryForeachParam.getSize()==null) {
                categoryForeachParam.setSize(DEFAULT_SIZE);
            }else if(categoryForeachParam.getSize()<1) {
                categoryForeachParam.setSize(1);
            }
            Long parentId = categoryForeachParam.getParentid();
            //根据所属父节点获取栏目列表
            if(parentId!=null) {
                return categoryService.getSubCategories(parentId,categoryForeachParam.getSize());
            }
            //获取当前站点
            if (StringUtils.isEmpty(categoryForeachParam.getSiteid())) {
                Site site = (Site) VariableExpression.getVariable(context, "site");
                categoryForeachParam.setSiteid(site.getSiteId());
            }
            //根据指定routeType获取栏目列表(默认返回导航栏目)
            RouteType routeType = categoryForeachParam.getRoutetype();
            if(routeType==null) {
                Route route = (Route)VariableExpression.getVariable(context,"route");
                categoryForeachParam.setRoutetype(route.getRouteType());
            }
            return categoryService.getGivenTypeCategories(categoryForeachParam);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
