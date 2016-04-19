package com.huotu.hotcms.service.common;

/**
 * <p>
 *     错误页面类型
 * </p>
 * @since xhl
 */
public enum PageErrorType  implements CommonEnum {
    NO_FIND_404(0,"/template/error/404.html"),
    BUDDING_500(1,"/template/error/budding.html"),
    CALLBACK(2,"/template/error/callback.html");

    private int code;
    private String value;

    PageErrorType(int code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public Object getCode() {
        return code;
    }

    @Override
    public String getValue() {
        return value;
    }
}
