package com.huotu.hotcms.web.util;

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

    /*
    * @param url
    * @param 正则表达式规则
    * @return 返回ID
    * */
    public Integer getUrlId(String url,String regexp)
    {
        Pattern pattern = Pattern.compile(regexp);
        Matcher match = pattern.matcher(url);
        if(match.matches())
        {
            if(match.groupCount()>1)
            {
                return Integer.parseInt(match.group(1));
            }
        }
        return null;
    }
}
