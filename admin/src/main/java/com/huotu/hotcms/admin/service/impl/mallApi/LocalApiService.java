package com.huotu.hotcms.admin.service.impl.mallApi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;


/**
 * 本地测试的对接MallApi接口
 * @author xhl
 */
@Service
@Profile("!container")
public class LocalApiService extends  AbstractApiService  {
    private static final Log log = LogFactory.getLog(DynamicApiService.class);

    @Autowired
    public void setWebApplicationContext(WebApplicationContext context){
        this.serviceRoot="http://devmallapi.huobanj.cn";

    }
}
