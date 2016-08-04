/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service.impl;

import com.huotu.hotcms.widget.PageTheme;
import com.huotu.hotcms.widget.service.CSSService;
import com.huotu.hotcms.widget.test.TestBase;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by lhx on 2016/6/3.
 */
@Transactional
public class CSSServiceTest extends TestBase {
    @Autowired
    CSSService cssService;

    @Test
    public void TestConvertCss() throws IOException {
        //--case1 --pageTheme为null
        try {
            cssService.convertCss(null, new ByteArrayOutputStream());
            Assert.assertEquals("断言程序case1错误", 0, 1);
        } catch (IOException e) {
            Assert.assertEquals("断言程序case1抛出异常错误", 0, 1);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("断言程序case1正确", 0, 0);
        }

        //--case2 --用户自定义less为null
        try {
            cssService.convertCss(new PageTheme() {
                @Override
                public String mainColor() {
                    return "#000000";
                }

                @Override
                public Resource customLess() {
                    return null;
                }
            },new ByteArrayOutputStream());
            Assert.assertEquals("断言程序case2错误", 0, 1);
        } catch (IOException e) {
            Assert.assertEquals("断言程序case2抛出异常错误", 0, 1);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("断言程序case2正确", 0, 0);
        }

        //--case3 --css输出文件为null
        try {
            cssService.convertCss(new PageTheme() {
                @Override
                public String mainColor() {
                    return null;
                }

                @Override
                public Resource customLess() {
                    String less = "@spanColor:100px;";
                    return new ByteArrayResource(less.getBytes());
                }
            }, null);
            Assert.assertEquals("断言程序case3错误", 0, 1);
        } catch (IOException e) {
            Assert.assertEquals("断言程序case3抛出异常错误", 0, 1);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("断言程序case3正确", 0, 0);
        }

        //--case4-- mainColor为null生成的css是否符合与预期用户定义less样式
        ByteArrayOutputStream byteOut1 = new ByteArrayOutputStream();
        try {
            cssService.convertCss(new PageTheme() {
                @Override
                public String mainColor() {
                    return null;
                }

                @Override
                public Resource customLess() {
                    String less = "@spanColor:#111;span{color:@spanColor}";
                    return new ByteArrayResource(less.getBytes());
                }
            }, byteOut1);
            String css = byteOut1.toString();
            if (css.contains("span") && css.contains("color: #111")){
                Assert.assertEquals("断言程序case4正确", 0, 0);
            }else{
                Assert.assertEquals("断言程序case4生成css与预期结果不一致", 0, 1);
            }
        } catch (Exception e) {
            Assert.assertEquals("断言程序case4抛出异常错误", 0, 1);
        }

        //--case5 --页面主题与用户都存在时，生成的css是否符合预期结果
        ByteArrayOutputStream byteOut2 = new ByteArrayOutputStream();
        try {
            cssService.convertCss(new PageTheme() {
                @Override
                public String mainColor() {
                    return "#000000";
                }

                @Override
                public Resource customLess() {
                    String less = "@spanWidth:100px;span{color:@mainColor;width:@spanWidth;}";
                    return new ByteArrayResource(less.getBytes());
                }
            }, byteOut2);
            String css = byteOut2.toString();
            if (css.contains("color: #000000;") && css.contains("width: 100px;")){
                Assert.assertEquals("断言程序case5正确css与预期结果一致", 0, 0);
            }else{
                Assert.assertEquals("断言程序case5生成css与预期结果不一致", 0, 1);
            }
        } catch (Exception e) {
            Assert.assertEquals("断言程序case5抛出异常错误", 0, 1);
        }
    }
}
