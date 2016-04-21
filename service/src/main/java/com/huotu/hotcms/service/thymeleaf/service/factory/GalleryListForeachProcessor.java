/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.service.factory;

import com.huotu.hotcms.service.entity.GalleryList;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.thymeleaf.foreach.PageableForeachParam;
import com.huotu.hotcms.service.service.GalleryListService;
import com.huotu.hotcms.service.thymeleaf.expression.DialectAttributeFactory;
import com.huotu.hotcms.service.thymeleaf.expression.VariableExpression;
import com.huotu.hotcms.service.thymeleaf.model.PageModel;
import com.huotu.hotcms.service.thymeleaf.model.RequestModel;
import com.huotu.hotcms.service.util.PatternMatchUtil;
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
 * Created by cwb on 2016/3/23.
 */
@Component
public class GalleryListForeachProcessor {

    private static Log log = LogFactory.getLog(GalleryListForeachProcessor.class);
    private final int DEFAULT_PAGE_NO = 1;
    private final int DEFAULT_PAGE_SIZE = 12;
    private final int DEFAULT_PAGE_NUMBER = 5;
    @Autowired
    private GalleryListService galleryListService;

    public Object process(IProcessableElementTag elementTag,ITemplateContext context) {
        Page<GalleryList> galleries = null;
        try {
            HttpServletRequest request = ((IWebContext)context).getRequest();
            String selvertUrl=PatternMatchUtil.getServletUrl(request);
            PageableForeachParam galleryListForeachParam = DialectAttributeFactory.getInstance()
                    .getForeachParam(elementTag, PageableForeachParam.class);
            Route route = (Route) VariableExpression.getVariable(context, "route");
            if(galleryListForeachParam.getPageno() == null) {
                if(StringUtils.isEmpty(request.getParameter("pageNo"))) {
                    galleryListForeachParam.setPageno(DEFAULT_PAGE_NO);
                }else {
                    int pageNo = Integer.parseInt(request.getParameter("pageNo"));
                    if(pageNo < 1) {
                        throw new Exception("页码小于1");
                    }
                    galleryListForeachParam.setPageno(pageNo);
                }
            }
            if(galleryListForeachParam.getPagesize() == null) {
                if(StringUtils.isEmpty(request.getParameter("pageSize"))) {
                    galleryListForeachParam.setPagesize(DEFAULT_PAGE_SIZE);
                }else {
                    int pageSize = Integer.parseInt(request.getParameter("pageSize"));
                    if(pageSize < 1) {
                        throw new Exception("请求数据列表容量小于1");
                    }
                    galleryListForeachParam.setPagesize(pageSize);
                }
            }
            if(galleryListForeachParam.getPagenumber() == null) {
                galleryListForeachParam.setPagenumber(DEFAULT_PAGE_NUMBER);
            }
            //根据当前请求的Uri来获得指定的ID
            if (galleryListForeachParam.getGalleryId() == null) {
                galleryListForeachParam.setGalleryId(PatternMatchUtil.getUrlIdByLongType(selvertUrl
                        , PatternMatchUtil.urlParamRegexp));
            }
            galleries = galleryListService.getGalleryList(galleryListForeachParam);
            //图片路径处理
            Site site = (Site)VariableExpression.getVariable(context,"site");
            for(GalleryList galleryList : galleries) {
                galleryList.setThumbUri(site.getResourceUrl() + galleryList.getThumbUri());
            }
            List<PageModel> pages = new ArrayList<>();
            int currentPage = galleryListForeachParam.getPageno();
            int totalPages = galleries.getTotalPages();
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
            requestModel.setHasNextPage(galleries.hasNext());
            if(galleries.hasNext()) {
                requestModel.setNextPageHref("?pageNo=" + (currentPage + 1));
            }
            if(galleries.hasPrevious()) {
                requestModel.setPrevPageHref("?pageNo=" + (currentPage - 1));
            }
            requestModel.setHasPrevPage(galleries.hasPrevious());
            requestModel.setCurrentPage(currentPage);
        }catch (Exception e) {
            log.error("galleryListForeach process-->"+e.getMessage());
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
