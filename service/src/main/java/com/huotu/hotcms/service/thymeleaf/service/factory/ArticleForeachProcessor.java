/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.service.factory;

import com.huotu.hotcms.service.common.RouteType;
import com.huotu.hotcms.service.entity.*;
import com.huotu.hotcms.service.model.thymeleaf.foreach.PageableForeachParam;
import com.huotu.hotcms.service.service.ArticleService;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.thymeleaf.expression.DialectAttributeFactory;
import com.huotu.hotcms.service.thymeleaf.expression.VariableExpression;
import com.huotu.hotcms.service.thymeleaf.model.PageModel;
import com.huotu.hotcms.service.thymeleaf.model.RequestModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.model.IProcessableElementTag;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwb on 2016/1/6.
 */
@Component
public class ArticleForeachProcessor {

    private static Log log = LogFactory.getLog(ArticleForeachProcessor.class);
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ArticleService articleService;

    @Autowired
    private DialectAttributeFactory dialectAttributeFactory;

    public Object process(IProcessableElementTag elementTag,ITemplateContext context) {
        Page<Article> articles = null;
        try {
            HttpServletRequest request = ((IWebContext)context).getRequest();
            PageableForeachParam articleForeachParam = dialectAttributeFactory.getForeachParam(elementTag
                    , PageableForeachParam.class);
            Route route = (Route) VariableExpression.getVariable(context, "route");
            if(StringUtils.isEmpty(articleForeachParam.getCategoryId())) {
                if(route.getRouteType()==RouteType.ARTICLE_LIST) {
                    Category current = categoryService.getCategoryByRoute(route);
                    articleForeachParam.setCategoryId(current.getId());
                }else{//通配规则ID
                    Long id=dialectAttributeFactory.getUrlId(request,route);
                    articleForeachParam.setCategoryId(id);
                }
            }
            //如果不是具体子栏目，应取得当前栏目所有一级子栏目数据列表
            if(StringUtils.isEmpty(articleForeachParam.getParentcId())) {
                if(route.getRouteType()!=RouteType.ARTICLE_LIST && route.getRouteType()!=RouteType.ARTICLE_CONTENT) {
                    Category current = categoryService.getCategoryByRoute(route);
                    articleForeachParam.setParentcId(current.getId());
                }
            }
            articleForeachParam=dialectAttributeFactory.getForeachParamByRequest(request, articleForeachParam);
            //TODO 代码重构，以下部分为1.0版本,这里保留是为了后面出现问题遗留,后面测试没问题后，直接移除
//            if(articleForeachParam.getPageNo() == null) {
//                if(StringUtils.isEmpty(request.getParameter("pageNo"))) {
//                    articleForeachParam.setPageNo(DEFAULT_PAGE_NO);
//                }else {
//                    int pageNo = Integer.parseInt(request.getParameter("pageNo"));
//                    if(pageNo < 1) {
//                        throw new Exception("页码小于1");
//                    }
//                    articleForeachParam.setPageNo(pageNo);
//                }
//            }
//            if(articleForeachParam.getPageSize() == null) {
//                if(StringUtils.isEmpty(request.getParameter("pageSize"))) {
//                    articleForeachParam.setPageSize(DEFAULT_PAGE_SIZE);
//                }else {
//                    int pageSize = Integer.parseInt(request.getParameter("pageSize"));
//                    if(pageSize < 1) {
//                        throw new Exception("请求数据列表容量小于1");
//                    }
//                    articleForeachParam.setPageSize(pageSize);
//                }
//            }
//            if(articleForeachParam.getPageNumber() == null) {
//                articleForeachParam.setPageNumber(DEFAULT_PAGE_NUMBER);
//            }
            articles = articleService.getArticleList(articleForeachParam);
            dialectAttributeFactory.setPageList(articleForeachParam,articles,context);
            //图片路径处理
            Site site = (Site)VariableExpression.getVariable(context,"site");
            for(Article article : articles) {
                article.setThumbUri(site.getResourceUrl() + article.getThumbUri());
            }
//            List<PageModel> pages = new ArrayList<>();
//            int currentPage = articleForeachParam.getPageNo();
//            int totalPages = articles.getTotalPages();
//            int pageNumber = articleForeachParam.getPageNumber() < totalPages ? articleForeachParam.getPageNumber() : totalPages;
//            int startPage = dialectAttributeFactory.calculateStartPageNo(currentPage,pageNumber,totalPages);
//            for(int i=1;i<=pageNumber;i++) {
//                PageModel pageModel = new PageModel();
//                pageModel.setPageNo(startPage);
//                pageModel.setPageHref("?pageNo=" + startPage);
//                pages.add(pageModel);
//                startPage++;
//            }
//            RequestModel requestModel = (RequestModel)VariableExpression.getVariable(context,"request");
//            requestModel.setPages(pages);
//            requestModel.setHasNextPage(articles.hasNext());
//            if(articles.hasNext()) {
//                requestModel.setNextPageHref("?pageNo=" + (currentPage + 1));
//            }
//            if(articles.hasPrevious()) {
//                requestModel.setPrevPageHref("?pageNo=" + (currentPage - 1));
//            }
//            requestModel.setHasPrevPage(articles.hasPrevious());
//            requestModel.setCurrentPage(currentPage);
        }catch (Exception e) {
            log.error("articleForeach process-->"+e.getMessage());
        }
        return articles;
    }
}
