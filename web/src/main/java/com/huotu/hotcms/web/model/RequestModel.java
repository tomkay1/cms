package com.huotu.hotcms.web.model;

import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator xhl 2016/1/7.
 */
@Getter
@Setter
public class RequestModel{
    private String hosts;

    private String url;

    private HttpServletRequest request;

    public String get(String param) {
        if (request != null) {
            return request.getParameter(param.toString());
        }
        return null;
    }
}
