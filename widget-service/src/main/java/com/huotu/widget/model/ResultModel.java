package com.huotu.widget.model;


/**
 * Created by hzbc on 2016/5/27.
 */

/**
 * 接口返回结果数据模型,包括：
 * <ul>
 *     <li>响应码-code</li>
 *     <li>响应信息-msg</li>
 *     <li>相应返回数据-data</li>
 * </ul>
 */
public class ResultModel {
    private final Integer code;
    private final String msg;
    private final Object data;

    public  ResultModel(int code,String msg,Object data)
    {
        this.code=code;
        this.msg=msg;
        this.data=data;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

}
