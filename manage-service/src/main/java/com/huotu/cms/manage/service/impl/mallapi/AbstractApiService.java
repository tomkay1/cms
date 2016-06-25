/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.service.impl.mallapi;

import com.huotu.cms.manage.service.MallApiService;
import com.huotu.hotcms.service.util.ApiResult;
import com.huotu.hotcms.service.util.HttpUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

/**
 * Created by Administrator on 2016/3/23.
 */
public  abstract class AbstractApiService implements MallApiService {
    private static final Log log = LogFactory.getLog(AbstractApiService.class);

    protected  String serviceRoot;


    @Override
    public ApiResult<String> HttpGet(String path,Map<String, Object> params) {
        try {
            String url=this.serviceRoot+path;
            return HttpUtils.httpGet(url,params);
        } catch (Exception e) {
            log.error("请求接口失败", e);
            throw new InternalError("请求接口失败");
        }
    }

    @Override
    public ApiResult<String> HttpPost(String path, Map<String, Object> params) {
        try {
            String url="";
            url=this.serviceRoot+path;
            return HttpUtils.httpPost(url,params);
        } catch (Exception e) {
            log.error("请求接口失败", e);
            throw new InternalError("请求接口失败");
        }
    }
}
