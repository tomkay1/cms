/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.service.factory;

import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.thymeleaf.foreach.PageableForeachParam;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.GalleryService;
import com.huotu.hotcms.service.thymeleaf.expression.DialectAttributeFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.model.IProcessableElementTag;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by cwb on 2016/3/23.
 */
@Component
public class GalleryForeachProcessor {

    private static Log log = LogFactory.getLog(GalleryForeachProcessor.class);

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private GalleryService galleryService;

    @Autowired
    private DialectAttributeFactory dialectAttributeFactory;

    public Object process(IProcessableElementTag elementTag,ITemplateContext context) {
        Page<Gallery> galleries = null;
        try {
            HttpServletRequest request = ((IWebContext)context).getRequest();
            PageableForeachParam galleryForeachParam =dialectAttributeFactory.getForeachParam(elementTag
                    , PageableForeachParam.class);
//            Route route = (Route) VariableExpression.getVariable(context, "route");
//            Route route=(Route)context.getVariable("route");
//            if(StringUtils.isEmpty(galleryForeachParam.getCategoryId())) {
//                if(route.getRouteType()== RouteType.GALLERY_CONTENT) {
//                    Category current = categoryService.getCategoryByRoute(route);
//                    galleryForeachParam.setCategoryId(current.getId());
//                }
//            }
//            //如果不是具体子栏目，应取得当前栏目所有一级子栏目数据列表
//            if(StringUtils.isEmpty(galleryForeachParam.getParentcId())) {
//                if(route.getRouteType()!=RouteType.GALLERY_CONTENT && route.getRouteType()!=RouteType.GALLERY_CONTENT) {
//                    Category current = categoryService.getCategoryByRoute(route);
//                    galleryForeachParam.setParentcId(current.getId());
//                }
//            }
            galleryForeachParam=dialectAttributeFactory.getForeachParamByRequest(request, galleryForeachParam);
            galleries = galleryService.getGalleryList(galleryForeachParam);
            //图片路径处理
            Site site=(Site)context.getVariable("site");
            for(Gallery gallery : galleries) {
                gallery.setThumbUri(site.getResourceUrl() + gallery.getThumbUri());
            }
            dialectAttributeFactory.setPageList(galleryForeachParam,galleries,context);
//            List<PageModel> pages = new ArrayList<>();
//            int currentPage = galleryForeachParam.getPageNo();
//            int totalPages = galleries.getTotalPages();
//            int pageNumber = galleryForeachParam.getPageNumber() < totalPages ? galleryForeachParam.getPageNumber() : totalPages;
//            int startPage = calculateStartPageNo(currentPage,pageNumber,totalPages);
//            for(int i=1;i<=pageNumber;i++) {
//                PageModel pageModel = new PageModel();
//                pageModel.setPageNo(startPage);
//                pageModel.setPageHref("?pageNo=" + startPage);
//                pages.add(pageModel);
//                startPage++;
//            }
//            RequestModel requestModel = (RequestModel)VariableExpression.getVariable(context,"request");
//            requestModel.setPages(pages);
//            requestModel.setHasNextPage(galleries.hasNext());
//            if(galleries.hasNext()) {
//                requestModel.setNextPageHref("?pageNo=" + (currentPage + 1));
//            }
//            if(galleries.hasPrevious()) {
//                requestModel.setPrevPageHref("?pageNo=" + (currentPage - 1));
//            }
//            requestModel.setHasPrevPage(galleries.hasPrevious());
//            requestModel.setCurrentPage(currentPage);
        }catch (Exception e) {
            log.error("galleryForeach process-->"+e.getMessage());
        }
        return galleries;
    }

    private int calculateStartPageNo(int currentPage, int pageNumber, int totalPages) {
        if(pageNumber == totalPages) {
            return 1;
        }
        return currentPage - pageNumber + 1 < 1 ? 1 : currentPage - pageNumber + 1;
    }

}
