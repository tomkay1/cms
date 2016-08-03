package com.huotu.hotcms;

import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.WebResourceSet;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.EmptyResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.scan.Constants;
import org.apache.tomcat.util.scan.StandardJarScanFilter;

import javax.servlet.ServletException;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 内嵌tomcat 为控件浏览测试提供
 * Created by lhx on 2016/8/2.
 */
@Getter
@Setter
public class EmbeddedTomcat {
    private static final Log log = LogFactory.getLog(EmbeddedTomcat.class);
    private static int port = 9080;
    private Tomcat tomcat = new Tomcat();

    public EmbeddedTomcat(int port) {
        this.port = port;
    }

    private static File getRootFolder() {
        try {
            File root;
            String runningJarPath = EmbeddedTomcat.class.getProtectionDomain().getCodeSource()
                    .getLocation().toURI().getPath().replaceAll("\\\\", "/");
            int lastIndexOf = runningJarPath.lastIndexOf("/target/");
            if (lastIndexOf < 0) {
                root = new File("");
            } else {
                root = new File(runningJarPath.substring(0, lastIndexOf));
            }
            log.info("application resolved root folder: " + root.getAbsolutePath());
            return root;
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void start() throws Exception {
        try {
            File root = getRootFolder();
            System.setProperty("org.apache.catalina.startup.EXIT_ON_INIT_FAILURE", "true");
            Tomcat tomcat = new Tomcat();
            Path tempPath = Files.createTempDirectory("tomcat-base-dir");
            tomcat.setBaseDir(tempPath.toString());
            tomcat.setPort(port);
            File webContentFolder = new File(root.getAbsolutePath(), "src/main/webapp/");
            if (!webContentFolder.exists()) {
                webContentFolder = Files.createTempDirectory("default-doc-base").toFile();
            }
            StandardContext ctx = (StandardContext) tomcat.addWebapp("", webContentFolder.getAbsolutePath());
            ctx.setParentClassLoader(EmbeddedTomcat.class.getClassLoader());

            //Disable TLD scanning by default
            if (System.getProperty(Constants.SKIP_JARS_PROPERTY) == null && System.getProperty(Constants.SKIP_JARS_PROPERTY) == null) {
                log.info("disabling TLD scanning");
                StandardJarScanFilter jarScanFilter = (StandardJarScanFilter) ctx.getJarScanner().getJarScanFilter();
                jarScanFilter.setTldSkip("*");
            }

            log.info("configuring app with basedir: " + webContentFolder.getAbsolutePath());

            // Declare an alternative location for your "WEB-INF/classes" dir
            // Servlet 3.0 annotation will work
            File additionWebInfClassesFolder = new File(root.getAbsolutePath(), "target/classes");
            WebResourceRoot resources = new StandardRoot(ctx);

            WebResourceSet resourceSet;
            if (additionWebInfClassesFolder.exists()) {
                resourceSet = new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClassesFolder.getAbsolutePath(), "/");
                log.info("loading WEB-INF resources from as '" + additionWebInfClassesFolder.getAbsolutePath() + "'");
            } else {
                resourceSet = new EmptyResourceSet(resources);
            }
            resources.addPreResources(resourceSet);
            ctx.setResources(resources);
            tomcat.start();
            tomcat.getServer().await();
        } catch (LifecycleException | ServletException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    public void stop() throws Exception {
        try {
            tomcat.stop();
            log.info("Tomcat stoped");
        } catch (LifecycleException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }


}
