/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.authorize.aop;

import com.huotu.cms.manage.authorize.annoation.AuthorizeRole;
import com.huotu.cms.manage.util.web.CookieUser;
import com.huotu.cms.manage.util.web.QueryHelper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * <p>
 * 授权切入点
 * </p>
 *
 * @author xhl
 *
 * @since 1.2
 */
@Aspect
public class AuthorizeAspect {

    private CookieUser cookieUser;

    public AuthorizeAspect(CookieUser cookieUser) {
        this.cookieUser = cookieUser;
//        System.out.print("初始化....");
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    public static AuthorizeRole.Role getControllerMethodRole(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    return method.getAnnotation(AuthorizeRole.class).roleType();
                }
            }
        }
        return null;
    }

    @Pointcut("@annotation(com.huotu.hotcms.admin.annoation.AuthorizeRole)")
    public void authorizeAspect() {
//        System.out.print("命中切点...");
    }

//    @After("authorizeAspect()")
//    public void doAfter(JoinPoint joinPoint) {
//        System.out.print("命中之后...");
//    }

    /**
     * 前置通知,用于授权验证
     *
     * @param joinPoint
     */
    @Before("authorizeAspect()")
    public void doBefore(JoinPoint joinPoint) {
        try {
            // 超级管理员
            // site host
            // abc.com/manage
            // A/manage
            Boolean isRole=true;
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            Integer customerId = QueryHelper.getQueryValInteger(request, "customerid");
            AuthorizeRole.Role role = getControllerMethodRole(joinPoint);
            if (role.equals(AuthorizeRole.Role.Owner)) {
                isRole=cookieUser.checkLogin(request, response, customerId);
            } else if (role.equals(AuthorizeRole.Role.Supper)) {
                isRole=cookieUser.isSupper(request);
            }
            if(!isRole)
                throw new ExceptionInInitializerError("没有权限");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
