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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermissions;

/**
 * Created by lhx on 2016/6/2.
 */
@Service
public class CSSServiceImpl implements CSSService {
    private static final Log log = LogFactory.getLog(CSSServiceImpl.class);

    @Override
    public void convertCss(PageTheme theme, OutputStream outputStream) throws IOException, IllegalArgumentException {
        String os = System.getProperties().getProperty("os.name");
        if (theme == null || outputStream == null || theme.customLess() == null) {
            throw new IllegalArgumentException();
        }
        // 创建临时 less文件和待输出css文件
        Path lessPath = Files.createTempFile("tempLess", ".less");
        try {
            InputStream lessInputStream = theme.customLess().getInputStream();// 用户自定义的less数据
            if (theme.mainColor() != null) {
                String mainColor = "@mainColor:" + theme.mainColor() + ";";
                //进行内容合并
                InputStream colorIs = new ByteArrayInputStream(mainColor.getBytes());
                SequenceInputStream sequenceInputStream = new SequenceInputStream(colorIs, lessInputStream);
                Files.copy(sequenceInputStream, lessPath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.copy(lessInputStream, lessPath, StandardCopyOption.REPLACE_EXISTING);
            }
            // 创建临时 css文件
            Path cssPath = Files.createTempFile("tempCss", ".css");
            try {
                Path shellPath;
                Process process;
                log.debug("current OS:" + os);
                if (os.contains("Mac") || os.contains("Linux") || os.contains("linux") || os.contains("CentOS")) {
                    // 创建临时 .sh文件并赋予文件内容和执行权限
                    shellPath = Files.createTempFile("tempLess2css", ".sh");
                    Files.copy(new ClassPathResource("less2css.sh").getInputStream(), shellPath, StandardCopyOption.REPLACE_EXISTING);
                    Files.setPosixFilePermissions(shellPath, PosixFilePermissions.fromString("rwxrwx---"));
                    //执行生成sh脚本生成css文件
                    process = Runtime.getRuntime().exec("sh " + shellPath.toFile().getAbsolutePath() + " "
                            + lessPath.toFile().getAbsolutePath() + " " + cssPath.toFile().getAbsolutePath());
                } else {
                    shellPath = Files.createTempFile("tempLess2css", ".bat");
                    Files.copy(new ClassPathResource("less2css.bat").getInputStream(), shellPath, StandardCopyOption.REPLACE_EXISTING);
                    process = Runtime.getRuntime().exec(shellPath.toFile().getAbsolutePath() + " " + lessPath.toFile().getAbsolutePath() + " "
                            + cssPath.toFile().getAbsolutePath());
                }
                try {
                    //额外工作
                    StreamUtils.copy(process.getInputStream(), System.out);//控制台输出.sh文件的输出
                    int status = -1;
                    try {
                        status = process.waitFor();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (status != 0) {
                        //删除临时文件
                        throw new IOException();
                    }
                    //读取临时文件写到输出流
                    Files.copy(cssPath, outputStream);
                } finally {
                    //noinspection ThrowFromFinallyBlock
                    Files.deleteIfExists(shellPath);
                }
            } finally {
                //noinspection ThrowFromFinallyBlock
                Files.deleteIfExists(cssPath);
            }
        } finally {
            //noinspection ThrowFromFinallyBlock
            Files.deleteIfExists(lessPath);
        }
    }
}
