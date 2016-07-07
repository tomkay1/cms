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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
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
     * URLClassLoader的addURL方法
     */
    private static Method addURL = initAddMethod();
    private static URLClassLoader system = (URLClassLoader) ClassLoader.getSystemClassLoader();

    /**
     * 初始化方法
     */
    private static Method initAddMethod() {
        try {
            Method add = URLClassLoader.class
                    .getDeclaredMethod("addURL", URL.class);
            add.setAccessible(true);
            return add;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static URLClassLoader getSystem() {
        return system;
    }

    /**
     * 循环遍历目录，找出所有的JAR包
     */
    private static void loopFiles(File file, List<File> files) {
        if (file.isDirectory()) {
            File[] tmps = file.listFiles();
            for (File tmp : tmps) {
                loopFiles(tmp, files);
            }
        } else {
            if (file.getAbsolutePath().endsWith(".jar") || file.getAbsolutePath().endsWith(".zip")) {
                files.add(file);
            }
        }
    }

    /**
     * <pre>
     * 加载JAR文件
     * </pre>
     *
     * @param jarUrl jar包的url
     */
    private static void loadJarFile(URL jarUrl) {
        try {
            addURL.invoke(system, jarUrl);
            log.info("Load：" + jarUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <pre>
     * 从一个目录加载所有JAR文件
     * </pre>
     *
     * @param path
     */
    public static void loadJarPath(String path) throws MalformedURLException {
        List<File> files = new ArrayList<>();
        File lib = new File(path);
        loopFiles(lib, files);
        for (File file : files) {
            loadJarFile(file.toURI().toURL());
        }
    }

    /**
     * 按照路径加载jar
     *
     * @param resource jar包资源
     */
    public static List<Class> loadJarWidgetClasses(Resource resource) throws IOException, FormatException {

        ClassLoaderUtil.loadJarFile(resource.getURL());
        List<Class> classes = null;

        Properties prop = new Properties();
        InputStream in = ClassLoaderUtil.class.getResourceAsStream("/META-INF/widget.properties");

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
                    Class<?> clazz = ClassLoaderUtil.getSystem().loadClass(className);
                    classes.add(clazz);
                }
            }
        } catch (ClassNotFoundException e) {
            throw new FormatException(e.toString());
        }
        return classes;
    }

/*    public static void main(String [] agrs) throws IOException {
        loadJarPath("E:/WorkSpace/MapSDKLibrary/libs/");
        Properties prop = new Properties();
        try {
            Class<?> clazz = system.loadClass("wusc.edu.facade.user.Util");
            Method method = clazz.getDeclaredMethod("getVersion");
            method.invoke(null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        InputStream in = ClassLoaderUtil.class.getResourceAsStream("/jdbc.properties");
        prop.load(in);
        //直接读取文件

        System.out.println(prop.getProperty("jdbc.url").trim());


        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();

            InputStream xmlIn = ClassLoaderUtil.class.getResourceAsStream("/META-INF/properties/spring-context2.xml");
        //    Document doc = db.parse(xmlIn);

            File f = new File("ts.xml");
            FileOutputStream fileOutputStream = new FileOutputStream(f);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }*/
}