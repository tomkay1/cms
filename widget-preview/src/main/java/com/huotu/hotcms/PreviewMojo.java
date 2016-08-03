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
            //robot 用来做什么我还不清楚，
            Robot robot = new Robot();
            InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
            Integer port = null;
            for (int i = 10000; i < 65535; i++) {
                try {
                    Socket socket = new Socket(inetAddress, i);
                    port = i;
                } catch (IOException e) {
                    //端口连接失败
                    getLog().info(i + "端口占用");
                }
            }
            EmbeddedTomcat tomcat = new EmbeddedTomcat(port.intValue());
            tomcat.start();
            Desktop desktop = Desktop.getDesktop();
            //todo 打开控件浏览视图或者编辑视图通过widgetName进行打开
            desktop.browse(new URI("http://localhost:" + port + "/"));

        } catch (Exception e) {
            getLog().error("preview", e);
        }
        getLog().info("preview");
    }
}
