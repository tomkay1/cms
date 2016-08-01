/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.util.regex.Pattern;

@Component
public class ConfigInfo {
    private final static Pattern debugPattern = Pattern.compile("-Xdebug|jdwp");
    private final String outLoginUrl;
    private final String mallManageUrl;
    private final String mallSupperUrl;
    private final boolean debugging;
    @Value("${resources.site}")
    private String resourcesSiteLogo;
    @Value("${resources.video}")
    private String resourcesVideo;
    @Value("${resources.ueditor}")
    private String resourcesUeditor;
    @Value("${resources.img}")
    private String resourcesImg;
    @Value("${resources.download}")
    private String resourcesDownload;
    @Value("${resources.widget}")
    private String resourcesWidget;
    @Value("${resources.config}")
    private String resourcesConfig;
    @Value("${resources.template}")
    private String resourcesTemplate;
    @Value("${resources.page}")
    private String pageConfig;

    @Autowired
    public ConfigInfo(ConfigService configService, Environment environment) {
        this.debugging = Debugging();
        this.outLoginUrl = environment.getProperty("cms.loginUrl", "http://manager." + configService.getMallDomain());
        this.mallManageUrl = environment.getProperty("huobanmall.mallManageUrl", "http://pdmall."
                + configService.getMallDomain() + "/home.aspx");
        this.mallSupperUrl = environment.getProperty("huobanmall.mallSupperUrl", "http://manager."
                + configService.getMallDomain() + "/home.aspx?customerid=%s");
    }

    private static boolean Debugging() {
        for (String arg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
            if (debugPattern.matcher(arg).find()) {
                return true;
            }
        }
        return false;
    }

    public String getOutLoginUrl() {
        return outLoginUrl;
    }

    /**
     * 商户伙伴商城后台地址入口
     */
    public String getMallManageUrl() {
        return mallManageUrl;
    }

    /**
     * 伙伴商城超级管理员后台地址入口
     */
    public String getMallSupperUrl(Integer customerId) {
        return String.format(mallSupperUrl, customerId);
    }

    public boolean isDebugging() {
        return debugging;
    }
}
