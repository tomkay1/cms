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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author CJ
 */
public class PreviewMojoTest {

    private static final Log log = LogFactory.getLog(PreviewMojoTest.class);

    private PreviewMojo mojo = new PreviewMojo();

    @Before
    public void init() {
        //  此处直接引入widget-test的测试控件即可
        Build build = new Build();
        File file = new File("../widget-test/target/test-classes");
        build.setOutputDirectory(file.getAbsolutePath());

        Model model = new Model();
        model.setBuild(build);

        MavenProject mavenProject = new MavenProject(model);

        HashMap<String, Object> context = new HashMap<>();
        context.put("project", mavenProject);
        mojo.setPluginContext(context);
    }

    @Test
    public void execute() throws MojoFailureException, MojoExecutionException {

        // 1 服务器就绪以后的driver检查
        long timeToClose = 60;
        // 2 安全时间以后(30s) 服务器必须被关闭
        ScheduledExecutorService executorService = java.util.concurrent.Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> {
            System.out.println("time up");
            try {
                mojo.shutdown();
            } catch (LifecycleException e) {
                log.error("", e);
            }
        }, timeToClose, TimeUnit.SECONDS);

        mojo.execute();
    }

}