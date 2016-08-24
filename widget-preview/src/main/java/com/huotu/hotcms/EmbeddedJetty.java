/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms;

import com.huotu.hotcms.config.DispatcherServletInitializer;
import com.huotu.hotcms.config.PreviewConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.maven.model.Model;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.annotations.ClassInheritanceHandler;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.JettyWebXmlConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;
import org.springframework.web.WebApplicationInitializer;

import java.io.File;
import java.net.URI;

/**
 * @author CJ
 */
class EmbeddedJetty implements ServletContainer {

    private static final Log log = LogFactory.getLog(EmbeddedJetty.class);
    private final int port;
    private final Model model;
    private Server server;

    /**
     * @param model 控件项目model
     * @param port  开启的port
     */
    EmbeddedJetty(Model model, int port) {
        this.port = port;
        this.model = model;
        System.setProperty("me.jiangcai.server.port", String.valueOf(port));
    }

    public void start() throws Exception {
        server = new Server(port);
        String output = model.getBuild().getOutputDirectory();
        log.debug("classes path:" + output);
        PreviewConfig.classesPath = new File(output);

        File base = new File(PreviewConfig.classesPath.getParentFile(), "jetty");

        File resources = new File(base, "_resources");
        System.setProperty("me.jiangcai.lib.resource.home", resources.toURI().toString());
        System.setProperty("me.jiangcai.lib.resource.http.uri", "http://localhost:" + port + "/_resources");

        URI webResourceBase = base.toURI();

        WebAppContext context = new WebAppContext();
        context.setBaseResource(Resource.newResource(webResourceBase));
        context.setConfigurations(new Configuration[]
                {
                        new AnnotationConfiguration() {
                            @Override
                            public void preConfigure(WebAppContext context) throws Exception {
                                ConcurrentHashSet<String> concurrentHashSet = new ConcurrentHashSet<>();
                                concurrentHashSet.add(DispatcherServletInitializer.class.getName());
                                ClassInheritanceMap map = new ClassInheritanceMap();
                                map.put(WebApplicationInitializer.class.getName(), concurrentHashSet);
                                context.setAttribute(CLASS_INHERITANCE_MAP, map);
                                _classInheritanceHandler = new ClassInheritanceHandler(map);
                            }
                        },
                        new WebInfConfiguration(),
                        new WebXmlConfiguration(),
                        new MetaInfConfiguration(),
                        new FragmentConfiguration(),
                        new EnvConfiguration(),
                        new PlusConfiguration(),
                        new JettyWebXmlConfiguration(),
                        new Configuration() {
                            @Override
                            public void preConfigure(WebAppContext context) throws Exception {

                            }

                            @Override
                            public void configure(WebAppContext context) throws Exception {
                                context.addAliasCheck((path, resource) -> true);
                            }

                            @Override
                            public void postConfigure(WebAppContext context) throws Exception {

                            }

                            @Override
                            public void deconfigure(WebAppContext context) throws Exception {

                            }

                            @Override
                            public void destroy(WebAppContext context) throws Exception {

                            }

                            @Override
                            public void cloneConfigure(WebAppContext template, WebAppContext context) throws Exception {

                            }
                        }
                });

        // It is important that this is last.
        ServletHolder holderPwd = new ServletHolder("default", DefaultServlet.class);
        holderPwd.setInitParameter("dirAllowed", "true");
        context.addServlet(holderPwd, "/");

        context.setContextPath("/");
        context.setParentLoaderPriority(true);


        server.setHandler(context);
        server.start();
//        server.dump(System.out);
        context.start();
    }

    @Override
    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void waitForStop() throws InterruptedException {
        server.join();
    }

}
