package com.huotu.cms.manage.interceptor;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by Administrator on 2015/12/18.
 */
@ControllerAdvice
public class ExceptionInterceptor {

    @ExceptionHandler(Throwable.class)
    public String catchExceptions(Throwable e) {
        e.printStackTrace();
        return "/view/error.html";
    }

}
