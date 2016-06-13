package com.huotu.hotcms.widget.service;

import com.huotu.hotcms.widget.util.ClassLoaderUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.*;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * applicationContext 切换的测试实现类 应结合实际情况进行重构后使用
 * Created by elvis on 2016/6/13.
 */
public class ChangeAC {

    /**
     * 测试本地切换ApplicationContext
     */
/*    public static void testLocalChangeCTX() {
        ApplicationContext parent = new ClassPathXmlApplicationContext("spring-context.xml");
        AnnotationConfigApplicationContext newApplicationContext = new AnnotationConfigApplicationContext(MVCConfigLocal.class);
        newApplicationContext.setParent(parent);
        //  newApplicationContext.refresh();
        // newApplicationContext.register(MVCConfig.class);
        newApplicationContext.start();
//        Te t = (Te)newApplicationContext.getBean("te");
//        t.sayHello();
    }*/


    /**
     * 把ApplicationContext 传入到jar 再返回的思路
     * 测试说明： 应该提供对应的jar在指定路径下   其中在包中有类centrelTest.ChangeAc//实现本地切换applicationContext 入参为applicationContext
     * 出参为AnnotationConfigApplicationContext
     */
    public static void testJarChangeCTX(){

        ApplicationContext parent = new ClassPathXmlApplicationContext("spring-context.xml");//// FIXME: 2016/6/13

        ClassLoaderUtil.loadJarPath("E:/WorkSpace/MapSDKLibrary/libs/");//// FIXME: 2016/6/13
        Class<?> clazz = null;
        try {
            clazz = ClassLoaderUtil.getSystem().loadClass("centrelTest.ChangeAc");
            Method method = clazz.getDeclaredMethod("testLocalChangeCTX",ApplicationContext.class);
            System.out.println(method.getParameterCount());
            Object invoke = method.invoke(ApplicationContext.class,parent);
            if(invoke instanceof AnnotationConfigApplicationContext){
                System.out.println("ok");
                AnnotationConfigApplicationContext newCtx = (AnnotationConfigApplicationContext) invoke;
                Object demoTest = newCtx.getBean("demoTest");

                System.out.println(demoTest);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 从jar 中读取JavaConfig 配置类，然后改变ApplicationContext
     *测试说明： 应该提供对应的jar在指定路径下   其中在包中有类centrelTest.MVCConfig 为javaConfig配置类
     */
    public static void testJarChangeCTX2(){

        ApplicationContext parent = new FileSystemXmlApplicationContext("spring-context.xml");//// FIXME: 2016/6/13

        ClassLoaderUtil.loadJarPath("E:/WorkSpace/MapSDKLibrary/libs/");//// FIXME: 2016/6/13
        Class<?> clazz = null;
        try {
            clazz = ClassLoaderUtil.getSystem().loadClass("centrelTest.MVCConfig");
            Method method = clazz.getDeclaredMethod("sayHello");
            method.invoke(null);
            AnnotationConfigApplicationContext newApplicationContext = new AnnotationConfigApplicationContext(clazz);
            newApplicationContext.setParent(null);
            //   newApplicationContext.refresh();
            //  newApplicationContext.register(clazz);
            newApplicationContext.start();
            Object t = newApplicationContext.getBean("demoTest");
            System.out.println(t);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void openSecurity(){
        System.out.println("Preparation step : turn on system permission check...");
        // 打开系统安全权限检查开关
        System.setSecurityManager(new SecurityManager());
    }

    public static void writeFile(){
        // 向文件写入内容(输出流)
        File file = new File("F:/ownwrite.txt");
        String str = "亲爱的小南瓜！";
        byte bt[] = new byte[1024];
        bt = str.getBytes();
        try {
            FileOutputStream in = new FileOutputStream(file);
            try {
                in.write(bt, 0, bt.length);
                in.close();
                // boolean success=true;
                // System.out.println("写入文件成功");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void privilegeReadFile(){
        AccessController.doPrivileged(new PrivilegedAction<String>() {
            public String run() {
                readFile();
                return null;
            }
        });

    }
    public static void readFile(){
        File f = new File("F:/test.txt");
        try {
            // 读取文件内容 (输入流)
            FileInputStream out = new FileInputStream(f);
            InputStreamReader isr = new InputStreamReader(out);
            int ch = 0;
            while ((ch = isr.read()) != -1) {
                System.out.print((char) ch);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 以权限策略方式执行文件写出
     * @throws IOException
     */
    public static void privilegeWriteFile(){
        AccessController.doPrivileged(new PrivilegedAction<String>() {
            public String run() {
                writeFile();
                return null;
            }
        });
    }

    /**
     *以权限策略方式切换AC
     * @throws IOException
     */
    public static void privilegChangeAC(){
        AccessController.doPrivileged(new PrivilegedAction<String>() {
            public String run() {
                testJarChangeCTX2();
                return null;
            }
        });
    }




    public static void main(String[] args) throws IOException {

//        openSecurity(); 权限开关

        //     System.out.println(System.getProperty("user.home"));

// testLoadClass();



        //非权限方式测试
        //testLocalChangeCTX();
        //testJarChangeCTX();
        testJarChangeCTX2();

        // writeFile();
        //readFile();

        //以权限方式测试
//        privilegeReadFile();
//        privilegeWriteFile();
        //       privilegChangeAC();


    }


}
