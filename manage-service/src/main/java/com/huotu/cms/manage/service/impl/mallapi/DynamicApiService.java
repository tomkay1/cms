/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.service.impl.mallapi;

import com.huotu.hotcms.service.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * 本地测试的对接MallApi接口
 *
 * @author xhl
 */
@Service
@Profile("container")
public class DynamicApiService extends AbstractApiService {

    @Autowired
    private void setEnv(ConfigService configService, Environment env) {
        this.serviceRoot = env.getProperty("huotu.mallApi", "http://mallapi." + configService.getMallDomain());
    }
}
