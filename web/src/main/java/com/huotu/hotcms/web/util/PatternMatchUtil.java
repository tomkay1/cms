package com.huotu.hotcms.web.util;

import javax.servlet.http.HttpServletRequest;
import java.beans.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 正则表达式操作类
 * </P>
 *
 * @author xhl
 * @since 1.0.0
 */
public class PatternMatchUtil {

    /**
     * 是否匹配路由规则
     *
     * @param url
     * @param regexp
     * @return
     * */
    public static boolean match(String url,String regexp)
    {
        Pattern pattern = Pattern.compile(regexp);
        Matcher match = pattern.matcher(url);
        return match.matches();
    }

    /**
     * 获得请求的Url
     *
     * @return
     * */
    public static String getUrl(HttpServletRequest request) {
        String queryString = request.getQueryString();
        String servletPath = request.getServletPath();
        if(queryString!=null) {
            return servletPath+"?"+queryString;
        }
        return servletPath;
    }

    /**
     * 根据url和路由规则来获得ID
     *
     * @param url
     * @param regexp
     * @return 返回ID
     * */
    public static Integer getUrlId(String url,String regexp)
    {
        Pattern pattern = Pattern.compile(regexp);
        Matcher match = pattern.matcher(url);
        if(match.matches()) {
            if(match.groupCount()>=1) {
                return Integer.parseInt(match.group(1));
            }
        }
        return null;
    }

    /**
     * 根据attributeValue 获得匹配的key值
     *
     * @param attributeValue
     * @param regexp
     * @return 返回ID
     * */
    public static String getMatchVal(String attributeValue,String regexp)
    {
        Pattern pattern = Pattern.compile(regexp);
        Matcher match = pattern.matcher(attributeValue);
        if(match.matches())
        {
            if(match.groupCount()>=1)
            {
                return match.group(1);
            }
        }
        return null;
    }
}
