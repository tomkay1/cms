/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.service;

import com.huotu.hotcms.service.util.ApiResult;

import java.util.Map;

/**
 * Created by Administrator on 2016/4/19.
 */
public interface MallApiService {
    ApiResult<String> HttpGet(String path, Map<String, Object> params);

    ApiResult<String> HttpPost(String path, Map<String, Object> params);
}
