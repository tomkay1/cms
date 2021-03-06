/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.service.factory;

import com.huotu.hotcms.service.common.RouteType;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.thymeleaf.foreach.CategoryForeachParam;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.thymeleaf.expression.DialectAttributeFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;

/**
 * Created by cwb on 2016/1/6.
 */
@Component
public class CategoryForeachProcessor {

    private static final Log log = LogFactory.getLog(CategoryForeachProcessor.class);
    private final int DEFAULT_SIZE = 6;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DialectAttributeFactory dialectAttributeFactory;

    public Object process(IProcessableElementTag elementTag, ITemplateContext context) {
        try {
            CategoryForeachParam categoryForeachParam = dialectAttributeFactory.getForeachParam(elementTag
                    , CategoryForeachParam.class);

            //根据指定id获取栏目列表
            if(!StringUtils.isEmpty(categoryForeachParam.getSpecifyIds())) {
                return categoryService.getSpecifyCategories(categoryForeachParam.getSpecifyIds());
            }
            //设置路由类型
//            Route route = (Route) VariableExpression.getVariable(context, "route");
//            Route route=(Route)context.getVariable("route");
//            if(categoryForeachParam.getRouteType()==null) {
//                categoryForeachParam.setRouteType(route.getRouteType());
//            }
            //设置站点id
            if (StringUtils.isEmpty(categoryForeachParam.getSiteId())) {
//                Site site = (Site) VariableExpression.getVariable(context, "site");
                Site site=(Site)context.getVariable("site");
                categoryForeachParam.setSiteId(site.getSiteId());
            }
//            if(isHeaderCategory(categoryForeachParam.getRouteType())) {
//                return categoryService.getHeaderCategoryList(categoryForeachParam);
//            }
            if(categoryForeachParam.getSize()==null) {
                categoryForeachParam.setSize(DEFAULT_SIZE);
            }else if(categoryForeachParam.getSize()<1) {
                categoryForeachParam.setSize(1);
            }
            //根据所属父节点及路由类型取得列表
            //设置父节点id
//            Long parentId = categoryForeachParam.getParentId();
//            if(parentId==null) {
//                Category current = categoryService.getCategoryByRoute(route);
//                categoryForeachParam.setParentId(current.getId());
//            }
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
