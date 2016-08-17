/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms;

import com.huotu.hotcms.config.PreviewConfig;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.maven.model.Model;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;

/**
 * 内置Tomcat引擎
 */
class EmbeddedTomcat {
    private static final Log log = LogFactory.getLog(EmbeddedTomcat.class);
    private final int port;
    private final Tomcat tomcat = new Tomcat();
    private final Model model;

    /**
     * @param model 控件项目model
     * @param port  开启的port
     */
    EmbeddedTomcat(Model model, int port) {
        this.port = port;
        this.model = model;
        System.setProperty("me.jiangcai.server.port", String.valueOf(port));
    }

    /**
     * 启动tomcat实例
     *
     * @throws LifecycleException
     * @throws IOException
     * @throws ServletException
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void start() throws LifecycleException, IOException, ServletException {

        String output = model.getBuild().getOutputDirectory();
        log.debug("classes path:" + output);
        PreviewConfig.classesPath = new File(output);

        System.setProperty("org.apache.catalina.startup.EXIT_ON_INIT_FAILURE", "true");
        File tomcatBase = new File(PreviewConfig.classesPath.getParentFile(), "tomcat");
        tomcatBase.mkdirs();
        tomcat.setBaseDir(tomcatBase.getAbsolutePath());
        tomcat.setPort(port);
        tomcat.getEngine().setName("CMS");

        File webContentFolder = new File(tomcatBase, "doc");
        webContentFolder.mkdir();
        tomcat.addWebapp("", webContentFolder.getAbsolutePath());
        log.info("tomcat start status await……");
        tomcat.start();
    }

    /**
     * 挂起当前线程,直到tomcat被关闭
     */
    void waitForTomcat() {
        tomcat.getServer().await();
    }

    void stop() throws LifecycleException {
        log.info("Stopping Tomcat");
        tomcat.stop();
    }


}
