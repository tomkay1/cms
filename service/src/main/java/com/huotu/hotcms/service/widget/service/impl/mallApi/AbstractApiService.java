package com.huotu.hotcms.service.widget.service.impl.mallApi;

import com.huotu.hotcms.service.util.ApiResult;
import com.huotu.hotcms.service.util.HttpUtils;
import com.huotu.hotcms.service.widget.service.MallApiEnvironmentService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

/**
 * Created by Administrator on 2016/3/23.
 */
public  abstract class AbstractApiService implements MallApiEnvironmentService {
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
