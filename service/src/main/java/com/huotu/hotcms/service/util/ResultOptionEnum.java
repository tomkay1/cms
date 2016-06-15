package com.huotu.hotcms.service.util;

import com.huotu.hotcms.service.common.CommonEnum;

/**
 * @since 1.0.0
 * @author xhl
 * @time 2015/15/25
 */
public enum ResultOptionEnum implements CommonEnum {
    OK(200,"成功"),
    NOLOGIN(201,"没有登录"),
    NO_LIMITS(202,"没有权限"),
    DOMAIN_EXIST(203,"域名已经存在"),
    ROUTE_EXISTS(204,"路由规则已经存在"),
    EXISTS_RELATION(205,"存在关联信息"),
    SYSTEM_ARTICLE(206,"系统文章不可做删除操作"),

    FILE_FORMATTER_ERROR(403,"文件格式错误"),
    NOFIND(404,"没有信息"),
    SITE_NOFIND(405,"站点不存在"),
    NOFIND_HOME_DEMON(406,"没有主推域名"),

    FAILE(500,"失败"),
    PARAMERROR(501,"参数错误"),
    SERVERFAILE(502,"服务器错误"),

    RESOURCE_OK(302,"成功"),
    RESOURCE_ERROR(500,"服务器错误"),
    RESOURCE_PERMISSION_ERROR(403,"缺少权限"),
    ;

    private int code;
    private String value;

    ResultOptionEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getValue() {
        return value;
    }

}
