package com.huotu.hotcms.web.service.impl.config;

import com.huotu.hotcms.web.service.ConfigService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by Administrator on 2016/3/23.
 */
public  abstract class AbstractConfigService implements ConfigService {
    private static final Log log = LogFactory.getLog(AbstractConfigService.class);

    protected String mallDomain;

    protected String mallResources;

    @Override
    public String getCustomerUri(String domain) {
        return "http://"+this.mallDomain+"."+domain;
    }

    @Override
    public String getImgUri(String domain) {
        return this.mallResources;
    }
}
