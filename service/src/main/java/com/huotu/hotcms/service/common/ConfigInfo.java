package com.huotu.hotcms.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by xhl on 2015/12/23.
 */
@Component
public class ConfigInfo {
    private  String outLoginUrl="";

    private  String mallManageUrl="";

    private  String mallSupperUrl="";

    @Autowired
    private Environment ev;

    @PostConstruct
    public void InitConfigInfo() {
        this.outLoginUrl = ev.getProperty("cms.loginUrl", "http://manager.51flashmall.com");
        this.mallSupperUrl = ev.getProperty("huobanmall.mallManageUrl", "http://pdmall.51flashmall.com/home.aspx");
        this.mallManageUrl = ev.getProperty("huobanmall.mallSupperUrl", "http://manager.51flashmall.com/home.aspx?customerid=%s");
    }

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

    @Value("${resources.appid}")
    private String appid;

    @Value("${resources.appsecret}")
    private String appsecret;


    public String getAppid() {
        return appid;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public String getResourcesSiteLogo(Integer customerId) {
        return String.format(resourcesSiteLogo,customerId) ;
    }

    public String getResourcesVideo(Integer customerId) {
        return String.format(resourcesVideo,customerId);
    }

    public String getResourcesUeditor(Integer customerId) {
         return String.format(resourcesUeditor,customerId);
    }

    public String getResourcesImg(Integer customerId) {
        return String.format(resourcesImg,customerId);
    }

    public String getResourcesDownload(Integer customerId) {
        return String.format(resourcesDownload,customerId);
    }

    /**
     * 获得控件主体地址目录
     * */
    public String getResourcesWidget(){
        return resourcesWidget;
    }

    public String getResourceWidgetImg(){
        return resourcesWidget+"/img";
    }

    /**
     * 获得商户装修的页面配置存储目录地址
     * */
    public String getResourcesConfig(Integer customerId,Long siteId){
        return String.format(resourcesConfig,customerId,siteId);
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
