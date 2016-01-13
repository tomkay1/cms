package com.huotu.hotcms.service.model;

import java.io.Serializable;

/**
 * Created by shiliting on 2015/6/25.
 * 该类是结果对象
 * @author shiliting
 */
public  class Result implements Serializable {

    private static final long serialVersionUID = -349012453592429794L;
    private int status;//状态信息
    private String message;//消息信息
    private Object body;//主体
    private int error;
    private String url;

    public Result() {

    }

    public Result(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
