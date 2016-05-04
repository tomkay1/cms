/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.service;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.thymeleaf.model.RequestModel;
import com.huotu.hotcms.service.util.PatternMatchUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator xhl 2016/1/7.
 */
@Component
public class RequestService {

    /**
     * 重新包装request对象,以达到扩展cms自己的内置对象交给前端模版使用request对象获得相关的信息,
     * 比如获得分页相关的信息以及网站语言版本的根地址信息等
     *
     * @param request
     * @param site
     * @return
     */
    public RequestModel ConvertRequestModel(HttpServletRequest request,Site site){
        RequestModel model=new RequestModel();
        model.setUrl(PatternMatchUtil.getServletUrl(request));
        model.setRequest(request);
        String contextPath=request.getContextPath();
        if(!StringUtils.isEmpty(contextPath)){
            model.setContextPath(contextPath);
        }else{
            model.setContextPath(null);
        }
        if(site!=null){
            model.setRoot(site,request);
            model.setRootUri(site, request);
            model.setResourcesUri(site.getResourceUrl());
        }
        return model;
    }

    public RequestModel ConvertRequestModel(HttpServletRequest request){
        RequestModel model=new RequestModel();
        model.setRequest(request);
        model.setRoot();
        return model;
    }

    public RequestModel ConvertRequestModelByError(HttpServletRequest request){
        RequestModel model=new RequestModel();
        String contextPath=request.getContextPath();
        if(!StringUtils.isEmpty(contextPath)){
            model.setContextPath(contextPath);
        }else{
            model.setContextPath(null);
        }
        model.setRoot(request);
        return model;
    }
}