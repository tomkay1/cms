package com.huotu.hotcms.widget.test;

import com.huotu.hotcms.widget.PageTheme;
import com.huotu.hotcms.widget.service.CSSService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Created by lhx on 2016/6/3.
 */

public class CSSServiceImplTest extends TestBase {
    @Autowired
    CSSService cssService;

    public void delTempFile(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void TestConvertCss() throws IOException {
        //--case1 --pageTheme为null
        Path cssPath1 = Files.createTempFile("temp", ".css");
        try {
            cssService.convertCss(null, Files.newOutputStream(cssPath1, StandardOpenOption.CREATE));
            Assert.assertEquals("断言程序case1错误", 0, 1);
        } catch (IOException e) {
            Assert.assertEquals("断言程序case1抛出异常错误", 0, 1);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("断言程序case1正确", 0, 0);
        }
        delTempFile(cssPath1);

        //--case2 --用户自定义less为null
        Path cssPath2 = Files.createTempFile("temp", ".css");
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
            }, Files.newOutputStream(cssPath2, StandardOpenOption.CREATE));
            Assert.assertEquals("断言程序case2错误", 0, 1);
        } catch (IOException e) {
            Assert.assertEquals("断言程序case2抛出异常错误", 0, 1);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("断言程序case2正确", 0, 0);
        }
        delTempFile(cssPath2);

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
        Path cssPath4 = Files.createTempFile("temp", ".css");
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
            }, Files.newOutputStream(cssPath4, StandardOpenOption.CREATE));
            InputStream inputStream = new FileInputStream(cssPath4.toFile());
            StringBuilder sb = new StringBuilder();
            byte[] data = new byte[1024];
            int len;
            while ((len=inputStream.read(data))!=-1){
                sb.append(new String(data,0,len));
            }
            inputStream.close();
            if (sb.toString().contains("span") && sb.toString().contains("color: #111")){
                Assert.assertEquals("断言程序case4正确", 0, 0);
            }else{
                Assert.assertEquals("断言程序case4生成css与预期结果不一致", 0, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertEquals("断言程序case4抛出异常错误", 0, 1);
        }
        delTempFile(cssPath4);
        //--case5 --页面主题与用户都存在时，生成的css是否符合预期结果
        Path cssPath5 = Files.createTempFile("temp", ".css");
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
            }, Files.newOutputStream(cssPath5, StandardOpenOption.CREATE));
            InputStream inputStream = new FileInputStream(cssPath5.toFile());
            StringBuilder sb = new StringBuilder();
            byte[] data = new byte[1024];
            int len;
            while ((len=inputStream.read(data))!=-1){
                sb.append(new String(data,0,len));
            }
            inputStream.close();
            if (sb.toString().contains("color: #000000;") && sb.toString().contains("width: 100px;")){
                Assert.assertEquals("断言程序case5正确css与预期结果一致", 0, 0);
            }else{
                Assert.assertEquals("断言程序case5生成css与预期结果不一致", 0, 1);
            }
        } catch (Exception e) {
            Assert.assertEquals("断言程序case5抛出异常错误", 0, 1);
        }
        delTempFile(cssPath5);
    }
}
