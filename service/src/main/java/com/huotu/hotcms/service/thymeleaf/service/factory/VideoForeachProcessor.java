/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.service.factory;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.Video;
import com.huotu.hotcms.service.model.thymeleaf.foreach.PageableForeachParam;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.VideoService;
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
 * Created by cwb on 2016/1/15.
 */
@Component
public class VideoForeachProcessor {
    private static final Log log = LogFactory.getLog(VideoForeachProcessor.class);
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private VideoService videoService;

    @Autowired
    private DialectAttributeFactory dialectAttributeFactory;

    public Object process(IProcessableElementTag elementTag,ITemplateContext context) {
        Page<Video> videoPage = null;
        try {
            PageableForeachParam videoForeachParam = dialectAttributeFactory.getForeachParam(elementTag
                    , PageableForeachParam.class);

            HttpServletRequest request = ((IWebContext)context).getRequest();
//            Route route = (Route) VariableExpression.getVariable(context, "route");
//            Route route=(Route) context.getVariable("route");
//            Category current = categoryService.getCategoryByRoute(route);
//            if(StringUtils.isEmpty(videoForeachParam.getCategoryId())) {
//                if(route.getRouteType()== RouteType.VIDEO_LIST) {
//                    videoForeachParam.setCategoryId(current.getId());
//                }
//            }
//            if(StringUtils.isEmpty(videoForeachParam.getParentcId())) {
//                //如果不是具体子栏目，应取得当前栏目所有一级子栏目数据列表
//                if(route.getRouteType()!=RouteType.VIDEO_LIST) {
//                    videoForeachParam.setParentcId(current.getId());
//                }
//            }
            videoForeachParam=dialectAttributeFactory.getForeachParamByRequest(request,videoForeachParam);
            videoPage = videoService.getVideoList(videoForeachParam);
            //图片路径处理
//            Site site = (Site)VariableExpression.getVariable(context,"site");
            Site site=(Site)context.getVariable("site");
            for(Video video : videoPage) {
                video.setThumbUri(site.getResourceUrl()+video.getThumbUri());
            }
            dialectAttributeFactory.setPageList(videoForeachParam,videoPage,context);
        }catch (Exception e) {
            log.error(e.getMessage());
        }
        return videoPage;
    }
}
