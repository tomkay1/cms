package com.huotu.hotcms.web.service;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.web.model.RequestModel;
import com.huotu.hotcms.web.util.PatternMatchUtil;
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