/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms;

import org.apache.catalina.LifecycleException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.awt.*;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;

/**
 * 执行预览
 */
@Mojo(name = "preview", requiresDependencyResolution = ResolutionScope.COMPILE)
public class PreviewMojo extends AbstractMojo {

    private EmbeddedTomcat tomcat;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            int port = 0;
            for (int i = 10000; i < 65535; i++) {
                try {
                    Socket socket = new Socket();
                    socket.bind(new InetSocketAddress(inetAddress, i));
                    socket.close();
                    port = i;
                    break;
                } catch (BindException ignored) {
                    // so continue
                }
            }

            getLog().info("Preview Http Server will use port " + port);
            MavenProject project = (MavenProject) getPluginContext().get("project");
            tomcat = new EmbeddedTomcat(project.getModel(), port);
            tomcat.start();
            getLog().info("-------------close command [Ctrl+c]-----------------");
            getLog().info("Browse " + "http://localhost:" + port + "/index");
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                desktop.browse(new URI("http://localhost:" + port + "/index"));
            }
            tomcat.waitForTomcat();
        } catch (Exception e) {
            getLog().error("preview", e);
        }
    }

    void shutdown() throws LifecycleException {
        tomcat.stop();
    }
}
