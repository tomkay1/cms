package com.huotu.hotcms.service.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**<p>
 * 检查手机设备帮助类
 * </p>
 *
 * @since 1.0.0
 *
 * @author xhl
 */
public class CheckMobile {
    private static final Log log = LogFactory.getLog(CheckMobile.class);

    // \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔),
    // 字符串在编译时会被转码一次,所以是 "\\b"
    // \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)
    static String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i"
            +"|windows (phone|ce)|blackberry"
            +"|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp"
            +"|laystation portable)|nokia|fennec|htc[-_]"
            +"|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
    static String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser"
            +"|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";

    //移动设备正则匹配：手机端、平板
    static Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);
    static Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);

    /**
     * 检测是否是移动设备访问
     *
     * @param userAgent 浏览器标识
     * @return true:移动设备接入，false:pc端接入
     */
    private static boolean check(String userAgent){
        if(null == userAgent){
            userAgent = "";
        }
        // 匹配
        Matcher matcherPhone = phonePat.matcher(userAgent);
        Matcher matcherTable = tablePat.matcher(userAgent);
        if(matcherPhone.find() || matcherTable.find()){
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查访问方式是否为移动端
     *
     * @param request
     * @return true:移动设备接入，false:pc端接入
     */
    public static boolean check(HttpServletRequest request){
        boolean isFromMobile=false;
        HttpSession session= request.getSession();
        //检查是否已经记录访问方式（移动端或pc端）
        if(null==session.getAttribute("ua")){
            try{
                //获取ua，用来判断是否为移动端访问
                String userAgent = request.getHeader( "USER-AGENT" ).toLowerCase();
                if(null == userAgent){
                    userAgent = "";
                }
                isFromMobile=check(userAgent);
                //判断是否为移动端访问
                if(isFromMobile){
                    session.setAttribute("ua","mobile");
                } else {
                    session.setAttribute("ua","pc");
                }
            }catch(Exception e){
            }
        }else{
            isFromMobile=session.getAttribute("ua").equals("mobile");
        }
        return isFromMobile;
    }

    /**
     * 根据环境获得手机官网地址,默认m.xxx.xx规则,比如m.site.com
     *
     * @param request
     */
    public static String getMobileUrl(HttpServletRequest request){
        boolean isMobile = CheckMobile.check(request);
        if (isMobile) {
            String url = request.getRequestURL().toString();
            String uri = request.getRequestURI();
            String domain = url.replace(uri, "");
            String mobileUrl = domain.replace("www", "m");
            return mobileUrl;
        }
        return null;
    }
}
