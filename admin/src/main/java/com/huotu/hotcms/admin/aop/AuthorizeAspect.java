package com.huotu.hotcms.admin.aop;

import com.huotu.hotcms.admin.annoation.AuthorizeRole;
import com.huotu.hotcms.admin.util.web.CookieUser;
import com.huotu.hotcms.admin.util.web.QueryHelper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
 * @since 1.2
 */
@Aspect
public class AuthorizeAspect {

    private CookieUser cookieUser;

    public AuthorizeAspect(CookieUser cookieUser) {
        this.cookieUser = cookieUser;
//        System.out.print("初始化....");
    }

    @Pointcut("@annotation(com.huotu.hotcms.admin.annoation.AuthorizeRole)")
    public void authorizeAspect() {
        System.out.print("命中切点...");
    }

    /**
     * 前置通知,用于授权验证
     *
     * @param joinPoint
     */
    @Before("authorizeAspect()")
    public void doBefore(JoinPoint joinPoint) {
        try {
            Boolean isRole=true;
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            Integer customerId = QueryHelper.getQueryValInteger(request, "customerid");
            AuthorizeRole.Role role = getControllerMethodRole(joinPoint);
            if (role.equals(AuthorizeRole.Role.Customer)) {
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

    @After("authorizeAspect()")
    public void doAfter(JoinPoint joinPoint) {
        System.out.print("命中之后...");
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
}
