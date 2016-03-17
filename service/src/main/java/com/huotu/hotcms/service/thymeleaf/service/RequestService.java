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
}