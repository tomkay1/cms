package com.huotu.hotcms.service.widget.service;

import com.huotu.hotcms.service.util.ApiResult;

import java.util.Map;

/**
 * Created by Administrator on 2016/3/23.
 */
public interface MallApiEnvironmentService {

    //    String uploadPhoto(Integer customerId,String base64Image,String size,String extendName);
    ApiResult<String> HttpGet(String path, Map<String, Object> params);

    ApiResult<String> HttpPost(String path, Map<String, Object> params);
}
