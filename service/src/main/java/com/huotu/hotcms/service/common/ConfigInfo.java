package com.huotu.hotcms.service.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by xhl on 2015/12/23.
 */
@Component
public class ConfigInfo {
    @Value("${out.loginUrl}")
    private String outLoginUrl;

    @Value("${resources.site}")
    private String resourcesSiteLogo;

    @Value("${resources.video}")
    private String resourcesVideo;

    @Value("${resources.ueditor}")
     private String resourcesUeditor;

    @Value("${resources.img}")
    private String resourcesImg;

    @Value("${out.mallManageUrl}")
    private String mallManageUrl;

    @Value("${out.mallSupperUrl}")
    private String mallSupperUrl;


    public String getResourcesSiteLogo() {
        return resourcesSiteLogo;
    }

    public String getResourcesVideo() {
        return resourcesVideo;
    }

    public String getResourcesUeditor() {
        return resourcesUeditor;
    }

    public String getResourcesImg() {
        return resourcesImg;
    }



    public String getOutLoginUrl() {
        return outLoginUrl;
    }

    /**
     * 商户伙伴商城后台地址入口
     * */
    public String getMallManageUrl(){return mallManageUrl;}

    /**
     * 伙伴商城超级管理员后台地址入口
     * */
    public String getMallSupperUrl(Integer customerId){
        return String.format(mallSupperUrl,customerId);
    }

}
