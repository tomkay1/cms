package com.huotu.hotcms.web.util;

import org.codehaus.plexus.util.StringUtils;

import java.util.Stack;

/**
 * <p>
 * 字符串处理类
 * </P>
 *
 * @author xhl
 * @since 1.0.0
 */
public class StringUtil {

    /**
     * 字符串转首字母转大写
     *
     * @param str
     * @return
     * */
    public static String toUpperCase(String str)
    {
        if(!StringUtils.isEmpty(str)) {
            if(str.length()>1) {
                return str.substring(0, 1).toUpperCase() + str.substring(1);
            }
            else{
                return str.substring(0, 1).toUpperCase();
            }
        }
        return null;
    }

    /**
     * 字符串移除开始和结束制定的字符串
     *
     * @param str
     * @param removeStr 要移除的前后字符串
     * @return
     * */
    public static  String Trim(String str,String removeStr){
        if(!StringUtils.isEmpty(str)){
            Integer index=str.lastIndexOf(removeStr);
            if(index>0) {
                str = str.substring(0, str.lastIndexOf(removeStr));
            }
            if(str.startsWith(removeStr)){
                str=str.substring(removeStr.length());
            }
        }
        return str;
    }
}
