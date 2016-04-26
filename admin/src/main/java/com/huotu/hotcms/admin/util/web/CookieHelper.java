package com.huotu.hotcms.admin.util.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 操作Cookie
 * Created by Administrator on 2015/5/21.
 */
public class CookieHelper {
    /**
     * 得到cookie的值，返回String
     *
     * @param request
     * @param key
     * @return
     */
    public static String getCookieVal(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 得到cookie的值，返回int
     *
     * @param request
     * @param key
     * @return
     */
    public static int getCookieValInteger(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    return Integer.parseInt(cookie.getValue());
                }
            }
        }
        return 0;
    }

    private static Cookie findCookie(HttpServletRequest request,String key){
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                   return cookie;
                }
            }
        }
        return null;
    }

    /**
     * 设置Cookie,存在则覆盖
     * @param request
     * @param response
     * @param key
     * @param value
     * @param maxAgeSecond
     */
    public static void setCookie(HttpServletRequest request,HttpServletResponse response,String key,String value,int maxAgeSecond){
        Cookie cookie=findCookie(request,key);
        if(cookie!=null){
            cookie.setValue(value);
            cookie.setMaxAge(maxAgeSecond);
            cookie.setPath("/");
            response.addCookie(cookie);
        }else{
            addCookie(response, key, value, maxAgeSecond);
        }
    }

//    /**
//     * 设置cookie
//     *
//     * @param response
//     * @param key
//     * @param value
//     */
//    public static void addCookie(HttpServletRequest request,HttpServletResponse response, String key, String value) {
//        Cookie cookie = new Cookie(key, value);
//        cookie.setMaxAge(1209600);
//        cookie.setPath("/");
//        response.addCookie(cookie);
//    }

    /**
     * 新增Cookie
     * @param response
     * @param key
     * @param value
     * @param maxAgeSecond
     */
    public static void addCookie(HttpServletResponse response,String key,String value,int maxAgeSecond)
    {
        Cookie cookie=new Cookie(key,value);
        cookie.setMaxAge(maxAgeSecond);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 删除cookie
     *
     * @param response
     * @param key
     */
    public static void removeCookie(HttpServletRequest request,HttpServletResponse response, String key) {
        Cookie cookie=findCookie(request,key);
        if(cookie!=null){
            cookie.setMaxAge(0);
        }
    }
}
