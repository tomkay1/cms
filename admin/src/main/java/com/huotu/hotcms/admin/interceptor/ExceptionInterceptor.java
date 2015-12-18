package com.huotu.hotcms.admin.interceptor;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by Administrator on 2015/12/18.
 */
@ControllerAdvice
public class ExceptionInterceptor {


    @ExceptionHandler(Throwable.class)
    public String catchExceptions(Throwable e,Model model) {
        model.addAttribute("errorInfo",e.getMessage());
        return "error.html";
    }

}
