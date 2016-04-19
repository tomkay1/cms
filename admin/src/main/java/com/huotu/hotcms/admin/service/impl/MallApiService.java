package com.huotu.hotcms.admin.service.impl;

import com.huotu.hotcms.service.util.ApiResult;

import java.util.Map;

/**
 * Created by Administrator on 2016/4/19.
 */
public interface MallApiService {
    ApiResult<String> HttpGet(String path, Map<String, Object> params);

    ApiResult<String> HttpPost(String path, Map<String, Object> params);
}
