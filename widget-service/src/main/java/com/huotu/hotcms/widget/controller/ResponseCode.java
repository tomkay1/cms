package com.huotu.hotcms.widget.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

/**
 * Created by hzbc on 2016/5/30.
 */
@ControllerAdvice
public class ResponseCode {
    /**
     *如果抛出{@link java.io.IOException},请求响应502
     */
    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public void occurIOException(){

    }
}
