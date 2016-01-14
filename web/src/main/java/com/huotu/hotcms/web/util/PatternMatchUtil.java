package com.huotu.hotcms.web.util;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.util.StringUtil;
import org.codehaus.plexus.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 正则表达式匹配处理操作类
 * </P>
 *
 * @author xhl
 * @since 1.0.0
 */
public class PatternMatchUtil {
    public static final String routeUrlRegexp="^/web(.*?)$";//

    public static final String webStringRegexp="^/web";//用于内部模版动态资源跳转路由mapping,CMS路由关键字

    public static final String templateRegexp="^/template";//用于内部模版静态资源跳转CMS路由关键字

    /**
     * 获得第一个语言参数
     * */
    public static final String langRegexp="^/([^/]+)";

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

    /**
     * 根据requst获得跟目录下的第一个参数
     * @param request
     * @return
     * **/
   public static String getLangParam(HttpServletRequest request){
        String path=request.getServletPath();
        String param= StringUtil.getIndex(path, 1, "/");
        if(!StringUtils.isEmpty(param)){
            if(param.equalsIgnoreCase("web")){
                param=StringUtil.getIndex(path,2,"/");
            }
        }
        return param;
    }

    /**
     * 根据requst获得有效语言参数
     *
     * @param request
     * @param site 站点信息
     * @return
     * **/
    public static  String getEffecterLangParam(HttpServletRequest request,Site site){
        if(site!=null) {
            String langParam = getLangParam(request);
            if(!StringUtils.isEmpty(langParam)) {
                if (langParam.contains("-")) {
                    String siteLang = site.getRegion().getLangCode() + "-" + site.getRegion().getRegionCode();
                    if (siteLang.equalsIgnoreCase(langParam)) {
                        return langParam;
                    }
                } else {
                    if (langParam.equalsIgnoreCase(site.getRegion().getRegionCode())) {
                        return langParam;
                    }
                }
            }
        }
        return null;
    }


    /**
     * 根据站点信息以及Request获得ServletPath(把语言相关参数移除)
     * **/
    public static String getServletPath(Site site,HttpServletRequest request){
        String path=request.getServletPath();
        if(site!=null){
           String langParam=getEffecterLangParam(request,site);
            if(!StringUtils.isEmpty(langParam)) {
                path=path.replace(langParam,"");
//                if (langParam.contains("-")) {
//                    if (site.getRegion() != null) {
//                        String siteLang = site.getRegion().getLangCode() + "-" + site.getRegion().getRegionCode();
//                        if (siteLang.equalsIgnoreCase(langParam)) {
//                            path=path.toLowerCase().replace(siteLang.toLowerCase(), "");
//                        }
//                    }
//                } else {
//                    if (site.getRegion() != null) {
//                        path= path.toLowerCase().replace(site.getRegion().getRegionCode().toLowerCase(), "");
//                    }
//                }
            }
        }
        path=path.replace("//","/");
        return path;
    }
}
