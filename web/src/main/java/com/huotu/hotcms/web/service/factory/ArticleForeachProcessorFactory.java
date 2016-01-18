/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.web.service.factory;

import com.huotu.hotcms.service.common.RouteType;
import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.thymeleaf.foreach.ArticleForeachParam;
import com.huotu.hotcms.service.service.ArticleService;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.web.model.PageModel;
import com.huotu.hotcms.web.model.RequestModel;
import com.huotu.hotcms.web.thymeleaf.expression.DialectAttributeFactory;
import com.huotu.hotcms.web.thymeleaf.expression.VariableExpression;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.model.IProcessableElementTag;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwb on 2016/1/6.
 */
public class ArticleForeachProcessorFactory {

    private final int DEFAULT_PAGE_NO = 1;
    private final int DEFAULT_PAGE_SIZE = 12;
    private final int DEFAULT_PAGE_NUMBER = 5;

    private static Log log = LogFactory.getLog(ArticleForeachProcessorFactory.class);

    private static ArticleForeachProcessorFactory instance;

    private ArticleForeachProcessorFactory() {
    }

    public static ArticleForeachProcessorFactory getInstance() {
        if(instance == null) {
            instance = new ArticleForeachProcessorFactory();
        }
        return instance;
    }

    public Object process(IProcessableElementTag elementTag,ITemplateContext context) {
        Page<Article> articles = null;
        try {
            WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
            HttpServletRequest request = ((IWebContext)context).getRequest();
            ArticleForeachParam articleForeachParam = DialectAttributeFactory.getInstance().getForeachParam(elementTag, ArticleForeachParam.class);
            Route route = (Route)VariableExpression.getVariable(context,"route");
            CategoryService categoryService = (CategoryService)applicationContext.getBean("categoryServiceImpl");
            Category current = categoryService.getCategoryByRoute(route);
            if(StringUtils.isEmpty(articleForeachParam.getCategoryid())) {
                //如果不是具体子栏目，应取得当前栏目所有一级子栏目数据列表
                if(route.getRouteType()==RouteType.ARTICLE_LIST) {
                    articleForeachParam.setCategoryid(current.getId());
                }
            }
            if(StringUtils.isEmpty(articleForeachParam.getParentcid())) {
                if(route.getRouteType()!=RouteType.ARTICLE_LIST) {
                    articleForeachParam.setParentcid(current.getId());
                }
            }
            if(articleForeachParam.getPageno() == null) {
                if(StringUtils.isEmpty(request.getParameter("pageNo"))) {
                    articleForeachParam.setPageno(DEFAULT_PAGE_NO);
                }else {
                    int pageNo = Integer.parseInt(request.getParameter("pageNo"));
                    if(pageNo < 1) {
                        throw new Exception("页码小于1");
                    }
                    articleForeachParam.setPageno(pageNo);
                }
            }
            if(articleForeachParam.getPagesize() == null) {
                if(StringUtils.isEmpty(request.getParameter("pageSize"))) {
                    articleForeachParam.setPagesize(DEFAULT_PAGE_SIZE);
                }else {
                    int pageSize = Integer.parseInt(request.getParameter("pageSize"));
                    if(pageSize < 1) {
                        throw new Exception("请求数据列表容量小于1");
                    }
                    articleForeachParam.setPagesize(pageSize);
                }
            }
            if(articleForeachParam.getPagenumber() == null) {
                articleForeachParam.setPagenumber(DEFAULT_PAGE_NUMBER);
            }
            ArticleService articleService = (ArticleService)applicationContext.getBean("articleServiceImpl");
            articles = articleService.getArticleList(articleForeachParam);
            //图片路径处理
            Site site = (Site)VariableExpression.getVariable(context,"site");
            for(Article article : articles) {
                article.setThumbUri(site.getResourceUrl() + article.getThumbUri());
            }
            List<PageModel> pages = new ArrayList<>();
            int currentPage = articleForeachParam.getPageno();
            int totalPages = articles.getTotalPages();
            int pageNumber = DEFAULT_PAGE_NUMBER < totalPages ? DEFAULT_PAGE_NUMBER : totalPages;
            int startPage = calculateStartPageNo(currentPage,pageNumber,totalPages);
            for(int i=1;i<=pageNumber;i++) {
                PageModel pageModel = new PageModel();
                pageModel.setPageNo(startPage);
                pageModel.setPageHref("?pageNo=" + startPage);
                pages.add(pageModel);
                startPage++;
            }
            RequestModel requestModel = (RequestModel)VariableExpression.getVariable(context,"request");
            requestModel.setPages(pages);
            requestModel.setHasNextPage(articles.hasNext());
            if(articles.hasNext()) {
                requestModel.setNextPageHref("?pageNo=" + (currentPage + 1));
            }
            if(articles.hasPrevious()) {
                requestModel.setPrevPageHref("?pageNo=" + (currentPage - 1));
            }
            requestModel.setHasPrevPage(articles.hasPrevious());
            requestModel.setCurrentPage(currentPage);
        }catch (Exception e) {
            log.error(e.getMessage());
        }

        return articles;
    }


    private int calculateStartPageNo(int currentPage, int pageNumber, int totalPages) {
        if(pageNumber == totalPages) {
            return 1;
        }
        return currentPage - pageNumber + 1 < 1 ? 1 : currentPage - pageNumber + 1;
    }

}
