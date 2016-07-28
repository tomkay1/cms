/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ConfigInfo {
    private final String outLoginUrl;

    private final String mallManageUrl;

    private final String mallSupperUrl;

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
        this.outLoginUrl = environment.getProperty("cms.loginUrl", "http://manager." + configService.getMallDomain());
        this.mallManageUrl = environment.getProperty("huobanmall.mallManageUrl", "http://pdmall."
                + configService.getMallDomain() + "/home.aspx");
        this.mallSupperUrl = environment.getProperty("huobanmall.mallSupperUrl", "http://manager."
                + configService.getMallDomain() + "/home.aspx?customerid=%s");
    }

    public String getResourcesSiteLogo(long ownerId) {
        return String.format(resourcesSiteLogo, ownerId);
    }

    public String getResourcesVideo(long ownerId) {
        return String.format(resourcesVideo, ownerId);
    }

    public String getResourcesUeditor(long ownerId) {
        return String.format(resourcesUeditor, ownerId);
    }

    public String getResourcesImg(long ownerId) {
        return String.format(resourcesImg, ownerId);
    }

    public String getResourcesDownload(long ownerId) {
        return String.format(resourcesDownload, ownerId);
    }

    public String getResourcesTemplate() {
        return resourcesTemplate;
    }

    public String getPageConfig(String pageId) {
        return String.format(pageConfig, pageId);
    }

    /**
     * 获得控件主体地址目录
     */
    public String getResourcesWidget() {
        return resourcesWidget;
    }

    public String getResourceWidgetImg() {
        return resourcesWidget + "/img";
    }

    /**
     * 获得商户装修的页面配置存储目录地址
     *
     * @param site 站点
     */
    public String getResourcesConfig(Site site) {
        return String.format(resourcesConfig, site.getOwner().getId(), site.getSiteId());
    }

    public String getTemplateConfig(Long siteId) {
        return String.format(resourcesTemplate, siteId);
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

}
