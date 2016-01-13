package com.huotu.hotcms.web.model;


import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * Created by Administrator xhl 2016/1/7.
 */
public class RequestModel{

    private String hosts;
    private String url;

    private Page page;


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

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
