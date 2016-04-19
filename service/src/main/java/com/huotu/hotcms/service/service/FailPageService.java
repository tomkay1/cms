package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.common.PageErrorType;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

/**
 * Created by Administrator on 2016/4/11.
 */
public interface FailPageService {
    String getFailPageTemplate(ApplicationContext applicationContext, PageErrorType pageErrorType) throws IOException;

}
