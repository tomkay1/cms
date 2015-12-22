package com.huotu.hotcms.admin.web;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2015/12/22.
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
