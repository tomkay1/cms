/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.web.service.impl.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * 本地测试的对接MallApi接口
 * @author xhl
 */
@Service
@Profile("container")
public class DynamicConfigService extends AbstractConfigService {

    private static final Log log = LogFactory.getLog(DynamicConfigService.class);

    @Autowired
    private void setEnv(Environment env) {
        this.mallDomain = env.getProperty("mall.domain", "51flashmall.com");
        this.mallResources = env.getProperty("mall.resources", "http://res.51flashmall.com");
        if(this.mallDomain==null){
            throw new IllegalStateException("请设置mall.domain");
        }
        if(this.mallResources==null){
            throw new IllegalStateException("请设置mall.resources");
        }
    }
}
