package com.huotu.hotcms.web.model;


import com.huotu.hotcms.service.entity.Site;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * Created by Administrator xhl 2016/1/7.
 */
public class RequestModel{
    private String hosts;

    /**
     * 当前请求的相对目录Url
     * */
    private String url;

    private List<PageModel> pages;

    private int currentPage;

    private boolean hasNextPage;

    private String nextPageHref;

    private boolean hasPrevPage;

    private String prevPageHref;

    /**
     * 当前请求的根路径
     * */
    private String root;

    private String resourcesPath;

    public String getResourcesPath() {
        return resourcesPath;
    }

    public void setResourcesPath(String resourcesPath) {
        this.resourcesPath = resourcesPath;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(Site site,HttpServletRequest request) {
        String rootUrl="";
        if(site.isCustom()){
            if(this.getResourcesPath()!=null) {
                rootUrl = site.getCustomTemplateUrl()+this.getResourcesPath();
            }else{
                rootUrl = site.getCustomTemplateUrl();
            }
        }else {
            if(this.getResourcesPath()!=null) {
                rootUrl = "http://" + request.getServerName() + ":" + request.getServerPort()+this.getResourcesPath();
            }else{
                rootUrl = "http://" + request.getServerName() + ":" + request.getServerPort();
            }

        }
        this.root = rootUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    private HttpServletRequest request;

    public String get(String param) {
        if (request != null) {
            return request.getParameter(param);
        }
        return null;
    }

    public List<PageModel> getPages() {
        return pages;
    }

    public void setPages(List<PageModel> pages) {
        this.pages = pages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public boolean isHasPrevPage() {
        return hasPrevPage;
    }

    public void setHasPrevPage(boolean hasPrevPage) {
        this.hasPrevPage = hasPrevPage;
    }

    public String getNextPageHref() {
        return nextPageHref;
    }

    public void setNextPageHref(String nextPageHref) {
        this.nextPageHref = nextPageHref;
    }

    public String getPrevPageHref() {
        return prevPageHref;
    }

    public void setPrevPageHref(String prevPageHref) {
        this.prevPageHref = prevPageHref;
    }
}
