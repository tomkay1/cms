package com.huotu.hotcms.service.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static Map<String,Object> getFieldList(Object o){
        Map<String,Object> map=new HashMap<>();
        try {
            Field[] fields = o.getClass().getDeclaredFields();
            for (Field field : fields) {
                map.put(field.getName(),getFieldValueByName(field.getName(),o));
            }
        } catch (Exception e) {
            throw new ExceptionInInitializerError("映射获得失败");
        }
        return map;
    }
}

