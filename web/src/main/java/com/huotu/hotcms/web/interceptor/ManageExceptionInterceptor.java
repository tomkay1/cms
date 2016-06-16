package com.huotu.hotcms.web.interceptor;

import com.huotu.hotcms.web.util.ApiResult;
import com.huotu.hotcms.web.util.ResultCodeEnum;
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

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ApiResult catchExceptions(Throwable e,HttpServletRequest request,HttpServletResponse response) throws IOException {
        e.printStackTrace();
        return ApiResult.resultWith(ResultCodeEnum.SYSTEM_BAD_REQUEST, e.getMessage(), null);
    }

}
