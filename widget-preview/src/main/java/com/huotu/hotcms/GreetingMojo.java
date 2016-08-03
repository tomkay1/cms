package com.huotu.hotcms;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by lhx on 2016/8/2.
 */
@Mojo(name = "preview")
public class GreetingMojo extends AbstractMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            Robot robot = new Robot();
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(new URI("http://localhost:9080"));

        } catch (IOException | AWTException | URISyntaxException e) {
            getLog().error("preview", e);
        }
        getLog().info("preview");
    }
}
