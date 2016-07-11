/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.util;

import com.huotu.hotcms.widget.exception.FormatException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author elvis
 */
public final class ClassLoaderUtil {

    private static final Log log = LogFactory.getLog(ClassLoaderUtil.class);

    /**
     * <pre>
     * 加载JAR文件
     * </pre>
     *
     * @param jarUrl jar包的url
     * @return 装载了该jar包的ClassLoader
     */
    private static ClassLoader loadJarFile(URL jarUrl) {
        return new URLClassLoader(new URL[]{jarUrl}, Thread.currentThread().getContextClassLoader());
    }

    /**
     * 按照路径加载jar
     *
     * @param resource jar包资源
     * @return 如果没有声明任何widgetClasses则返回null
     */
    public static List<Class> loadJarWidgetClasses(Resource resource) throws IOException, FormatException {

        ClassLoader classLoader = ClassLoaderUtil.loadJarFile(resource.getURL());
        List<Class> classes = null;

        Properties prop = new Properties();
        InputStream in = classLoader.getResourceAsStream("META-INF/widget.properties");

        if (in == null) {
            throw new FormatException("this jar " + resource.getFile() + " format error");
        }
        prop.load(in);
        //直接读取文件
        String classNameStr = prop.getProperty("widgetClasses").trim();
        try {
            String[] classNameArr = classNameStr.split(",");
            if (classNameArr.length > 0) {
                classes = new ArrayList<>();
                for (String className : classNameArr) {
                    Class<?> clazz = classLoader.loadClass(className);
                    classes.add(clazz);
                }
            }
        } catch (ClassNotFoundException e) {
            throw new FormatException(e.toString(), e);
        }
        return classes;
    }

}