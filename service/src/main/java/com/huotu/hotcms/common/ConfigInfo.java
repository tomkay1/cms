package com.huotu.hotcms.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2015/12/23.
 */
@Component
public class ConfigInfo {
    @Value("${out.loginUrl}")
    private String outLoginUrl;

    public String getOutLoginUrl() {
        return outLoginUrl;
    }
}
