package com.huotu.hotcms.util;

import com.huotu.hotcms.common.CMSEnums;

/**
 * @since 1.0.0
 * @author xhl
 * @time 2015/15/25
 */
public class ResultView {
    private final   int code;
    private final String msg;
    private final Object data;

    public  ResultView(int code,String msg,Object data)
    {
        this.code=code;
        this.msg=msg;
        this.data=data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }
}
