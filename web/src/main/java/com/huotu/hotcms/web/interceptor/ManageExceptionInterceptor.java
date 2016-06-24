/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.web.interceptor;

import com.huotu.hotcms.web.util.ApiResult;
import com.huotu.hotcms.web.util.ResultCodeEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 2015/12/18.
 */
@ControllerAdvice
@Controller
public class ManageExceptionInterceptor {

    private static final Log log = LogFactory.getLog(ManageExceptionInterceptor.class);

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ApiResult catchExceptions(Throwable e,HttpServletRequest request,HttpServletResponse response) throws IOException {
        log.warn("unknown exception", e);
        return ApiResult.resultWith(ResultCodeEnum.SYSTEM_BAD_REQUEST, e.getMessage(), null);
    }

}
