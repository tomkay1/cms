package com.huotu.widget.service;

/**
 * Created by wenqi on 2016/5/31.
 */

/**
 * css相关服务
 */
public interface CSSService {
    /**
     * 将less文件编译成css文件
     * @param cssName 输出css文件路劲 全路劲
     * @param fileName less文件 全路劲
     */
    void convertLess2Css(String fileName,String cssName);

}
