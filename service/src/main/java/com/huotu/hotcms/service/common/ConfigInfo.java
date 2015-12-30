package com.huotu.hotcms.service.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by xhl on 2015/12/23.
 */
@Component
public class ConfigInfo {
    @Value("${out.loginUrl}")
    private String outLoginUrl;

    public String getOutLoginUrl() {
        return outLoginUrl;
    }
}
