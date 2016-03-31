package com.huotu.hotcms.service.util;

import org.codehaus.plexus.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
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

    /**
     * 字符串移除开始和结束制定的字符串
     *
     * @param str
     * @param index 获得制定索引下的分割内容
     * @param charStr 要分隔的字符串
     * @return
     * */
    public static String getIndex(String str,Integer index,String charStr){
        if(str.contains(charStr)){
            String[] list=str.split(charStr);
            if(list!=null){
                if(list.length>index){
                    return list[index];
                }
            }
        }
        return null;
    }

    /**
     * 根据requst获得跟目录下的第一个参数
     * @param path 要处理的字符串
     * @param ignoreStr 忽略的字符串
     * @return
     * **/
    public static String getFirstParam(String path,String ignoreStr){
        String param= StringUtil.getIndex(path, 1, "/");
        if(!StringUtils.isEmpty(param)){
            if(param.equalsIgnoreCase(ignoreStr)){
                param=StringUtil.getIndex(path,2,"/");
            }
        }
        return param;
    }


}
