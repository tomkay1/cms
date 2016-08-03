package com.huotu.hotcms.servlet;

import org.springframework.web.servlet.DispatcherServlet;

public class PreviewServlet extends DispatcherServlet {
    public static final String BROWSE = "/browse/";
    public static final String EDITOR = "/editor/";
    public static final String PREVIEW = "/preview/";

    public PreviewServlet() {
        super();
    }

}
