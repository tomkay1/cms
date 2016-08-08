package com.huotu.hotcms;

import com.huotu.hotcms.servlet.PreviewServlet;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import java.io.File;
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
    private int port = 9080;
    private Tomcat tomcat = new Tomcat();

    public EmbeddedTomcat(int port) {
        this.port = port;
    }

    public EmbeddedTomcat() {
    }

    public void start() throws Exception {
        try {
            System.setProperty("org.apache.catalina.startup.EXIT_ON_INIT_FAILURE", "true");
            Path tempPath = Files.createTempDirectory("tomcat-base-dir");
            tomcat.setBaseDir(tempPath.toString());
            tomcat.setPort(port);
            File webContentFolder = Files.createTempDirectory("default-doc-base").toFile();
            StandardContext ctx = (StandardContext) tomcat.addWebapp("", webContentFolder.getAbsolutePath());
            tomcat.addServlet(ctx, "PreviewServlet", new PreviewServlet());
            ctx.addServletMapping("/*", "PreviewServlet");
            log.info("tomcat start status await……");
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
