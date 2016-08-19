/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms;

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.junit.Test;

import java.io.File;

/**
 * @author CJ
 */
public class EmbeddedJettyTest {
    @Test
    public void start() throws Exception {
        //  此处直接引入widget-test的测试控件即可
        Build build = new Build();
        File file = new File("./target/test-classes");
        build.setOutputDirectory(file.getAbsolutePath());

        Model model = new Model();
        model.setBuild(build);

        new EmbeddedJetty(model, 9090).start();
    }

}