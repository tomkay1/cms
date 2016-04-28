/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.service.factory;

import com.huotu.hotcms.service.entity.Download;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.thymeleaf.foreach.NormalForeachParam;
import com.huotu.hotcms.service.model.thymeleaf.foreach.PageableForeachParam;
import com.huotu.hotcms.service.service.DownloadService;
import com.huotu.hotcms.service.thymeleaf.expression.DialectAttributeFactory;
import com.huotu.hotcms.service.thymeleaf.expression.VariableExpression;
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
import java.util.List;

/**
 * Created by cwb on 2016/1/18.
 */
@Component
public class DownloadForeachProcessor {

    private static Log log = LogFactory.getLog(DownloadForeachProcessor.class);
    private final int DEFAULT_PAGE_SIZE = 5;
    @Autowired
    private DownloadService downloadService;

    @Autowired
    private DialectAttributeFactory dialectAttributeFactory;

    public List<Download> process(IProcessableElementTag elementTag,ITemplateContext context) {
        Page<Download> downloads = null;
        try {
            HttpServletRequest request = ((IWebContext)context).getRequest();
            PageableForeachParam downloadForeachParam = dialectAttributeFactory.getForeachParam(elementTag
                    , PageableForeachParam.class);
            Route route = (Route) VariableExpression.getVariable(context, "route");
            if(route!=null){
                if(StringUtils.isEmpty(downloadForeachParam.getCategoryId())) {
                    Long id=dialectAttributeFactory.getUrlId(request, route);
                    if(id!=null){
                        downloadForeachParam.setCategoryId(id);
                    }
                }
            }
            downloadForeachParam=dialectAttributeFactory.getForeachParamByRequest(request, downloadForeachParam);
            downloads = downloadService.getDownloadList(downloadForeachParam);
            Site site = (Site) VariableExpression.getVariable(context, "site");
            for(Download article : downloads) {
                article.setDownloadUrl(site.getResourceUrl() + article.getDownloadUrl());
            }
            dialectAttributeFactory.setPageList(downloadForeachParam,downloads,context);
//            //根据指定id获取栏目列表
//            if(downloadForeachParam.getSpecifyIds()!=null) {
//                List<Download> downloads = downloadService.getSpecifyDownloads(downloadForeachParam.getSpecifyIds());
//                for(Download download : downloads) {
//                    download.setDownloadUrl(site.getResourceUrl() + download.getDownloadUrl());
//                }
//                return downloads;
//            }
//            if(StringUtils.isEmpty(downloadForeachParam.getCategoryId())) {
//                throw new Exception("栏目id没有指定");
//            }
//            if(downloadForeachParam.getSize()==null) {
//                downloadForeachParam.setSize(DEFAULT_PAGE_SIZE);
//            }
//            List<Download> downloads;
//            downloads = downloadService.getDownloadList(downloadForeachParam);
//            for(Download download : downloads) {
//                download.setDownloadUrl(site.getResourceUrl() + download.getDownloadUrl());
//            }
//            return downloads;
        }catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
