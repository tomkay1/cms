package com.huotu.hotcms.service.widget.service.impl.mallapi;

import com.huotu.hotcms.service.util.ApiResult;
import com.huotu.hotcms.service.util.HttpUtils;
import com.huotu.hotcms.service.widget.service.MallApiEnvironmentService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Created by Administrator on 2016/3/23.
 */
public  abstract class AbstractApiService implements MallApiEnvironmentService {
    private static final Log log = LogFactory.getLog(AbstractApiService.class);

    protected  String serviceHost;

    public String scheme;

    protected String mallHost;

    @Override
    public ApiResult<String> HttpGet(String path,Map<String, Object> params) {
        try {
            String url="";
            if(StringUtils.isEmpty(this.scheme)){
                url="http://"+this.serviceHost+path;
            }else{
                url=this.scheme+"://"+this.serviceHost+path;
            }
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
            if(StringUtils.isEmpty(this.scheme)){
                url="http://"+this.serviceHost+path;
            }else{
                url=this.scheme+"://"+this.serviceHost+path;
            }
            return HttpUtils.httpPost(url,params);
        } catch (Exception e) {
            log.error("请求接口失败", e);
            throw new InternalError("请求接口失败");
        }
    }

    @Override
    public String getCustomerUri(String domain) {
        return "http://"+this.mallHost+"."+domain;
    }
}
