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
    public static final String routeUrlRegexp="^/web(.*?)$";//

    public static final String webStringRegexp="^/web";//

    public static final String templateRegexp="^/template";


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

    public static boolean matchFind(String url,String regexp){
        Pattern pattern = Pattern.compile(regexp);
        Matcher match = pattern.matcher(url);
        return match.find();
    }

    /**
     * 根据url判断是否匹配过滤要求
     * */
    public static boolean isMatchFilter(String url){
        return !matchFind(url, templateRegexp)&&!matchFind(url, webStringRegexp);
    }

    /**
     * 获得请求的Url包含参数
     *
     * @return
     * */
    public static String getUrl(HttpServletRequest request) {
        String queryString = request.getQueryString();
        String servletPath = request.getServletPath();
        if(PatternMatchUtil.match(servletPath,PatternMatchUtil.routeUrlRegexp)) {
            servletPath = PatternMatchUtil.getUrlString(servletPath, PatternMatchUtil.routeUrlRegexp);
        }
        if(queryString!=null) {
            return servletPath+"?"+queryString;
        }
        return servletPath;
    }

    /**
     * 获得请求的Url不包含参数
     *
     * @return
     * */
    public static String getServletUrl(HttpServletRequest request){
        String servletPath = request.getServletPath();
        if(PatternMatchUtil.match(servletPath,PatternMatchUtil.routeUrlRegexp)) {
            servletPath = PatternMatchUtil.getUrlString(servletPath, PatternMatchUtil.routeUrlRegexp);
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
     * 根据url和路由规则来获得匹配项目
     *
     * @param url
     * @param regexp
     * @return 返回ID
     * */
    public static String getUrlString(String url,String regexp)
    {
        Pattern pattern = Pattern.compile(regexp);
        Matcher match = pattern.matcher(url);
        if(match.matches()) {
            if(match.groupCount()>=1) {
                return match.group(1);
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
