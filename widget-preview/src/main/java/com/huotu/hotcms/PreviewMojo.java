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
                }
            }
            getLog().debug("Preview Http Server will use port " + port);
            EmbeddedTomcat tomcat = new EmbeddedTomcat(port);
            tomcat.start();
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(new URI("http://localhost:" + port + "/index.html"));
        } catch (Exception e) {
            getLog().error("preview", e);
        }
    }
}
