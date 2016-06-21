package com.huotu.widget.test.bean;

import com.huotu.widget.test.service.WidgetConfiguration;

import java.util.Stack;

/**
 * Created by lhx on 2016/6/21.
 */

public class PublicStackHolder {
    private static final ThreadLocal<Stack<WidgetConfiguration>> models = new ThreadLocal<>();

    /**
     * 获取当前公共参数
     * @return 在controller级别操作 返回总不会为空
     */
    public static Stack getStack(){
        return models.get();
    }

    public static void putStack(Stack model){
        models.set(model);
    }
}
