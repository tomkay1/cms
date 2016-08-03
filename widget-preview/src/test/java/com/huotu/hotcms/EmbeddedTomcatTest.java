package com.huotu.hotcms;

import org.junit.Test;

/**
 * Created by lhx on 2016/8/3.
 */
public class EmbeddedTomcatTest {
    EmbeddedTomcat embeddedTomcat = new EmbeddedTomcat(9080);

    @Test
    public void start() throws Exception {
        embeddedTomcat.start();
    }

    @Test
    public void stop() throws Exception {
        embeddedTomcat.stop();
    }

}