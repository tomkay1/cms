package com.huotu.cms.manage.util.web;

import javax.servlet.http.HttpServletRequest;

/**
 * 获得参数相关帮助类
 * @since 1.0.0
 * @author xhl
 */
public class QueryHelper {

    /**
     * 获得参数值
     *
     * @param request
     * @param name
     * @return
     */
    public static int getQueryValInteger(HttpServletRequest request,String name)
    {
        try {
            String param = request.getParameter(name);
            return Integer.valueOf(param);
        }
        catch (Exception ex)
        {
            return  -1;
        }
    }

    /**
     * 获得参数值
     *
     * @param request
     * @param name
     * @return
     */
    public  static String getQueryValString(HttpServletRequest request,String name)
    {
        return  request.getParameter(name);
    }

}
