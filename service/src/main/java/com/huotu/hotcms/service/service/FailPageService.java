package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.common.PageErrorType;

import java.io.IOException;

/**
 * Created by Administrator on 2016/4/11.
 */
public interface FailPageService {
    public String getFailPageTemplate(PageErrorType pageErrorType) throws IOException;

}
