/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;

/**
 * Created by lhx on 2016/8/2.
 */
@Mojo(name = "preview")
public class PreviewMojo extends AbstractMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            int port = 0;
            for (int i = 10000; i < 65535; i++) {
                try {
                    new Socket(inetAddress, i);
                    port = i;
                } catch (IOException ignored) {
                    //端口连接失败
//                    getLog().info(i + "端口占用");
                }
            }
            getLog().debug("Preview Http Server will use port " + port);
            EmbeddedTomcat tomcat = new EmbeddedTomcat(port);
            tomcat.start();
            Desktop desktop = Desktop.getDesktop();


            //todo 打开控件浏览视图或者编辑视图通过widgetName进行打开
            desktop.browse(new URI("http://localhost:" + port + "/"));

        } catch (Exception e) {
            getLog().error("preview", e);
        }
    }
}
