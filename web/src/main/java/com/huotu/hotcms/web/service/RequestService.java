package com.huotu.hotcms.web.service;

import com.huotu.hotcms.web.model.RequestModel;
import com.huotu.hotcms.web.util.PatternMatchUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator xhl 2016/1/7.
 */
@Component
public class RequestService {
    public RequestModel ConvertRequestModel(HttpServletRequest request){
        RequestModel model=new RequestModel();
        model.setUrl(PatternMatchUtil.getServletUrl(request));
        model.setRequest(request);
        return model;
    }
}
