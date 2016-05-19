/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.service.factory;

import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.thymeleaf.foreach.NormalForeachParam;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.LinkService;
import com.huotu.hotcms.service.thymeleaf.expression.DialectAttributeFactory;
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


/**
 * Created by cwb on 2016/1/6.
 */
@Component
public class LinkForeachProcessor {

    private static Log log = LogFactory.getLog(LinkForeachProcessor.class);
    @Autowired
    private LinkService linkService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DialectAttributeFactory dialectAttributeFactory;

    public Object process(IProcessableElementTag elementTag, ITemplateContext context) {
        Page<Link> linkList = null;
        try {
            HttpServletRequest request = ((IWebContext)context).getRequest();
            NormalForeachParam linkForeachParam = dialectAttributeFactory.getForeachParam(elementTag
                    , NormalForeachParam.class);//为了兼容目前先保留
//            Route route = (Route) VariableExpression.getVariable(context, "route");
            Route route=(Route)context.getVariable("route");
            if(route!=null){
                if(StringUtils.isEmpty(linkForeachParam.getCategoryId())) {
                    Long id=dialectAttributeFactory.getUrlId(request,route);
                    if(id!=null){
                        linkForeachParam.setCategoryId(id);
                    }
                }
            }
            linkForeachParam=dialectAttributeFactory.getForeachParamByRequest(request, linkForeachParam);
            linkList = linkService.getLinkList(linkForeachParam);
//            Site site = (Site)VariableExpression.getVariable(context,"site");
            Site site=(Site)context.getVariable("site");
            for(Link article : linkList) {
                article.setThumbUri(site.getResourceUrl() + article.getThumbUri());
            }
            dialectAttributeFactory.setPageList(linkForeachParam,linkList,context);
//            List<PageModel> pages = new ArrayList<>();
//            int currentPage = linkForeachParam.getPageNo();
//            int totalPages = linkList.getTotalPages();
//            int pageNumber = linkForeachParam.getPageNumber() < totalPages ? linkForeachParam.getPageNumber() : totalPages;
//            int startPage = dialectAttributeFactory.calculateStartPageNo(currentPage, pageNumber, totalPages);
//            for(int i=1;i<=pageNumber;i++) {
//                PageModel pageModel = new PageModel();
//                pageModel.setPageNo(startPage);
//                pageModel.setPageHref("?pageNo=" + startPage);
//                pages.add(pageModel);
//                startPage++;
//            }
//            RequestModel requestModel = (RequestModel)VariableExpression.getVariable(context,"request");
//            requestModel.setPages(pages);
//            requestModel.setHasNextPage(linkList.hasNext());
//            if(linkList.hasNext()) {
//                requestModel.setNextPageHref("?pageNo=" + (currentPage + 1));
//            }
//            if(linkList.hasPrevious()) {
//                requestModel.setPrevPageHref("?pageNo=" + (currentPage - 1));
//            }
//            requestModel.setHasPrevPage(linkList.hasPrevious());
//            requestModel.setCurrentPage(currentPage);
//            //根据指定id获取栏目列表
//            if(linkForeachParam.getSpecifyIds()!=null) {
//                return linkService.getSpecifyLinks(linkForeachParam.getSpecifyIds());
//            }
//            if(StringUtils.isEmpty(linkForeachParam.getCategoryId())) {
//                throw new Exception("栏目id没有指定");
//            }
//            if(linkForeachParam.getSize()==null) {
//                linkForeachParam.setSize(DEFAULT_PAGE_SIZE);
//            }
//            Site site = (Site) VariableExpression.getVariable(context, "site");
//            linkList= linkService.getLinkList(linkForeachParam);
//            for(Link link : linkList) {
//                link.setThumbUri(site.getResourceUrl() + link.getThumbUri());
//            }
        }catch (Exception e) {
            log.error(e.getMessage());
        }
        return linkList;
    }
}
