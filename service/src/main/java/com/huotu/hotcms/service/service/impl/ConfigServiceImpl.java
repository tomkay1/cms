/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.service.ConfigService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * 本地测试的对接MallApi接口
 *
 * @author xhl
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    private static final Log log = LogFactory.getLog(ConfigServiceImpl.class);

    private final String mallDomain;
    private final String mallResourceURL;

    @Autowired
    public ConfigServiceImpl(Environment environment) {
        super();
        if (environment.acceptsProfiles("container") && environment.getProperty("mall.domain") == null)
            throw new IllegalStateException("mall.domain System Property is required.");
        mallDomain = environment.getProperty("mall.domain", "51flashmall.com");
        mallResourceURL = environment.getProperty("mall.resources", "http://res." + mallDomain);
    }

    @Override
    public String getMallResourceURL() {
        return mallResourceURL;
    }

    @Override
    public String getMallDomain() {
        return mallDomain;
    }

    @Override
    public String getCustomerUri(String domain) {
        return "http://" + domain + "." + this.mallDomain;
    }

    @Override
    public String getImgUri(String domain) {
        return getMallResourceURL();
    }
}
