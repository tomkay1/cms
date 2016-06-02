/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service;

import com.huotu.hotcms.widget.PageTheme;

import java.io.IOException;
import java.io.OutputStream;

/**
 * css相关服务
 */
public interface CSSService {
//    /**
//     * 将less文件编译成css文件
//     *
//     * @param cssName  输出css文件路劲 全路劲
//     * @param fileName less文件 全路劲
//     */
//    void convertLess2Css(String fileName, String cssName);

    /**
     * 将页面主题转换为既定规格的css
     *
     * @param theme        页面主题
     * @param outputStream css的输入流
     * @throws IOException              如果保存css发生错误
     * @throws IllegalArgumentException 主题信息不符合要求
     */
    void convertCss(PageTheme theme, OutputStream outputStream) throws IOException, IllegalArgumentException;

}
