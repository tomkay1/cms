package com.huotu.hotcms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * Created by lhx on 2016/8/3.
 */
public class EmbeddedTomcatTest {
    private static final Log log = LogFactory.getLog(EmbeddedTomcatTest.class);
    EmbeddedTomcat embeddedTomcat = new EmbeddedTomcat();

    @Test
    public void start() throws Exception {
        embeddedTomcat.setPort(9080);
        embeddedTomcat.start();
        embeddedTomcat.stop();
    }

    @Test
    public void stop() throws Exception {
        embeddedTomcat.stop();
    }

}