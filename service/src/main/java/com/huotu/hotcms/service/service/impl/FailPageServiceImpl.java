package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.common.PageErrorType;
import com.huotu.hotcms.service.service.FailPageService;
import com.huotu.hotcms.service.util.HttpUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/4/11.
 */
@Service
public class FailPageServiceImpl implements FailPageService {
    @Override
    public String getFailPageTemplate(PageErrorType pageErrorType) throws IOException {
        InputStream inputStream=getClass().getResourceAsStream(pageErrorType.getValue());
        byte[] getData = HttpUtils.readInputStream(inputStream); //获得网站的二进制数据
        String html = new String(getData,"utf-8");
        return html;
    }
}
