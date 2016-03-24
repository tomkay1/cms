/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.util.ApiResult;

import java.util.Map;

/**
 * 基于http请求的一些常用服务
 * Created by cwb on 2016/3/24.
 */
public interface HttpService {

    ApiResult<String> httpGet_prod(String scheme, String host, Integer port, String path, Map<String, Object> params) throws Exception;

    String createDigest(String appId,String random,String secret);

}
