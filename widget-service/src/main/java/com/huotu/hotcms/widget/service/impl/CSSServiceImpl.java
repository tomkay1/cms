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
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermissions;

/**
 * Created by lhx on 2016/6/2.
 */
@Service
public class CSSServiceImpl implements CSSService {

    @Override
    public void convertCss(PageTheme theme, OutputStream outputStream) throws IOException, IllegalArgumentException, InterruptedException {
        System.out.println("OS :" + System.getProperties().getProperty("os.name"));
        String mainColor = theme.mainColor();
        // 创建临时 less文件和待输出css文件
        Path lessPath = Files.createTempFile("tempLess", ".less");
        InputStream lessInputStream = theme.customLess().getInputStream();// 用户自定义的less数据
        if (mainColor != null) {
            //进行内容合并
            InputStream colorIs = new ByteArrayInputStream(mainColor.getBytes());
            SequenceInputStream sequenceInputStream = new SequenceInputStream(colorIs,lessInputStream);
            Files.copy(sequenceInputStream, lessPath, StandardCopyOption.REPLACE_EXISTING);
        }else {
            Files.copy(lessInputStream, lessPath, StandardCopyOption.REPLACE_EXISTING);
        }

        // 创建临时 .sh文件并赋予文件内容和执行权限
        Path shellPath = Files.createTempFile("tempLess2css", ".sh");
        Files.copy(new ClassPathResource("less2css.sh").getInputStream(), shellPath, StandardCopyOption.REPLACE_EXISTING);
        Files.setPosixFilePermissions(shellPath, PosixFilePermissions.fromString("rwxrwx---"));
        // 创建临时 css文件
        Path cssPath = Files.createTempFile("tempCss", ".css");

        //执行生成sh脚本生成css文件
        Process process = Runtime.getRuntime().exec("sh "+shellPath.toFile().getAbsolutePath() + " " + lessPath.toFile().getAbsolutePath() + " " + cssPath.toFile().getAbsolutePath());

        //额外工作
        StreamUtils.copy(process.getInputStream(), System.out);//控制台输出.sh文件的输出
        System.out.println("status:" + process.waitFor());// .sh文件执行结果 0 为正常执行，非0出错

        //读取临时文件写到输出流
        InputStream is = new FileInputStream(cssPath.toFile());
        byte[] data = new byte[1024];
        int len;
        while ((len = is.read(data)) != -1) {
            outputStream.write(data, 0, len);
        }
        outputStream.flush();
        outputStream.close();
        is.close();

        //删除临时文件
        Files.deleteIfExists(lessPath);
        Files.deleteIfExists(cssPath);
        Files.deleteIfExists(shellPath);
    }
}
