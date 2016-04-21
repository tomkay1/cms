/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.service.factory;

import com.huotu.hotcms.service.common.RouteType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.Video;
import com.huotu.hotcms.service.model.thymeleaf.foreach.PageableForeachParam;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.VideoService;
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
 * Created by cwb on 2016/1/15.
 */
@Component
public class VideoForeachProcessor {
    private static final Log log = LogFactory.getLog(VideoForeachProcessor.class);
    private final int DEFAULT_PAGE_NO = 1;
    private final int DEFAULT_PAGE_SIZE = 12;
    private final int DEFAULT_PAGE_NUMBER = 5;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private VideoService videoService;

    public Object process(IProcessableElementTag elementTag,ITemplateContext context) {
        Page<Video> videoPage = null;
        try {
            PageableForeachParam videoForeachParam = DialectAttributeFactory.getInstance().getForeachParam(elementTag
                    , PageableForeachParam.class);

            HttpServletRequest request = ((IWebContext)context).getRequest();
            Route route = (Route) VariableExpression.getVariable(context, "route");
            Category current = categoryService.getCategoryByRoute(route);
            if(StringUtils.isEmpty(videoForeachParam.getCategoryid())) {
                if(route.getRouteType()== RouteType.VIDEO_LIST) {
                    videoForeachParam.setCategoryid(current.getId());
                }
            }
            if(StringUtils.isEmpty(videoForeachParam.getParentcid())) {
                //如果不是具体子栏目，应取得当前栏目所有一级子栏目数据列表
                if(route.getRouteType()!=RouteType.VIDEO_LIST) {
                    videoForeachParam.setParentcid(current.getId());
                }
            }
            if(videoForeachParam.getPageno() == null) {
                if(StringUtils.isEmpty(request.getParameter("pageNo"))) {
                    videoForeachParam.setPageno(DEFAULT_PAGE_NO);
                }else {
                    int pageNo = Integer.parseInt(request.getParameter("pageNo"));
                    if(pageNo < 1) {
                        throw new Exception("页码小于1");
                    }
                    videoForeachParam.setPageno(pageNo);
                }
            }
            if(videoForeachParam.getPagesize() == null) {
                if(StringUtils.isEmpty(request.getParameter("pageSize"))) {
                    videoForeachParam.setPagesize(DEFAULT_PAGE_SIZE);
                }else {
                    int pageSize = Integer.parseInt(request.getParameter("pageSize"));
                    if(pageSize < 1) {
                        throw new Exception("请求数据列表容量小于1");
                    }
                    videoForeachParam.setPagesize(pageSize);
                }
            }
            if(videoForeachParam.getPagenumber() == null) {
                videoForeachParam.setPagenumber(DEFAULT_PAGE_NUMBER);
            }
            videoPage = videoService.getVideoList(videoForeachParam);
            //图片路径处理
            Site site = (Site)VariableExpression.getVariable(context,"site");
            for(Video video : videoPage) {
                video.setThumbUri(site.getResourceUrl()+video.getThumbUri());
            }
            //形成页码列表
            setPageList(videoForeachParam,videoPage,context);
        }catch (Exception e) {
            log.error(e.getMessage());
        }
        return videoPage;
    }

    private void setPageList(PageableForeachParam videoForeachParam,Page<Video> videoPage,ITemplateContext context) {
        int currentPage = videoForeachParam.getPageno();
        int totalPages = videoPage.getTotalPages();
        int pageNumber = DEFAULT_PAGE_NUMBER < totalPages ? DEFAULT_PAGE_NUMBER : totalPages;
        int startPage = calculateStartPageNo(currentPage,pageNumber,totalPages);
        List<PageModel> pages = new ArrayList<>();
        for(int i=1;i<=pageNumber;i++) {
            PageModel pageModel = new PageModel();
            pageModel.setPageNo(startPage);
            pageModel.setPageHref("?pageNo=" + startPage);
            pages.add(pageModel);
            startPage++;
        }
        RequestModel requestModel = (RequestModel)VariableExpression.getVariable(context,"request");
        requestModel.setPages(pages);
        requestModel.setHasNextPage(videoPage.hasNext());
        if(videoPage.hasNext()) {
            requestModel.setNextPageHref("?pageNo=" + (currentPage + 1));
        }
        if(videoPage.hasPrevious()) {
            requestModel.setPrevPageHref("?pageNo=" + (currentPage - 1));
        }
        requestModel.setHasPrevPage(videoPage.hasPrevious());
        requestModel.setCurrentPage(currentPage);
    }

    private int calculateStartPageNo(int currentPage, int pageNumber, int totalPages) {
        if(pageNumber == totalPages) {
            return 1;
        }
        return currentPage - pageNumber + 1 < 1 ? 1 : currentPage - pageNumber + 1;
    }
}
