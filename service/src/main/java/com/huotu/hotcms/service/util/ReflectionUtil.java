package com.huotu.hotcms.service.util;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/4/11.
 */
public  class ReflectionUtil {
    public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            throw new ExceptionInInitializerError("映射获得失败");
        }
    }
}
