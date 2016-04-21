/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.expression;

import com.huotu.hotcms.service.common.EnumUtils;
import com.huotu.hotcms.service.common.RouteType;
import com.huotu.hotcms.service.util.HttpUtils;
import com.huotu.hotcms.service.widget.model.GoodsSearcher;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IElementAttributes;
import org.thymeleaf.model.IProcessableElementTag;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by cwb on 2016/1/5.
 */
public class DialectAttributeFactory {

    private static DialectAttributeFactory instance;

    private DialectAttributeFactory() {
    }

    public static DialectAttributeFactory getInstance() {
        if (instance == null) {
            instance = new DialectAttributeFactory();
        }
        return instance;
    }

    public <T> T getForeachParam(IProcessableElementTag elementTag, Class<T> t) throws Exception {
        T obj = t.newInstance();
        IElementAttributes elementAttributes = elementTag.getAttributes();
        List<AttributeName> attributeNames = elementAttributes.getAllAttributeNames();
        for (AttributeName attr : attributeNames) {
            String paramValue = elementAttributes.getValue(attr);
            try {
                Field field = t.getDeclaredField(attr.getAttributeName());
                field.setAccessible(true);
                Class<?> classType = field.getType();
                if (classType == Integer.class) {
                    field.set(obj, Integer.parseInt(paramValue));
                } else if (classType == Long.class) {
                    field.set(obj, Long.parseLong(paramValue));
                } else if (classType == Double.class) {
                    field.set(obj, Double.parseDouble(paramValue));
                } else if (classType == String.class) {
                    field.set(obj, paramValue);
                } else if (classType == String[].class) {
                    field.set(obj, paramValue.split(","));
                } else if (classType == RouteType.class) {
                    field.set(obj, EnumUtils.valueOf(RouteType.class, Integer.parseInt(paramValue)));
                } else {
                    field.set(obj, paramValue);
                }
            } catch (NoSuchFieldException ignored) {
            }
        }
        return obj;
    }

    /**
     * 从界面有两种方式可以拿到数据，一种通过一个请求，一种通过自定义标签；
     * 此方法用于对比两种方法拿到的数据的优先级，即如果两种方法都拿到同一个属性的值，则优先选择请求中的;如果各不相同，则融合在一个对象中，并返回。
     *
     * @param elementTag         元素标签
     * @param httpServletRequest http请求
     * @param clz                对应的class类型
     * @param <T>                泛型，需要返回的对应的类型
     * @return 返回相应的类
     * @throws Exception 发生各种异常
     */
    public <T> T megerObject(IProcessableElementTag elementTag, HttpServletRequest httpServletRequest, Class<T> clz)
            throws Exception {

        T obj1 = DialectAttributeFactory.getInstance().getForeachParam(elementTag, clz);//自定义字段中读取到的
        T obj2 = HttpUtils.getRequestParam(httpServletRequest, clz); //从一起请求中读取到

        Field[] obj1Fields = obj1.getClass().getDeclaredFields();
        Field[] obj2Fields = obj2.getClass().getDeclaredFields();
        for (Field obj1Field : obj1Fields) {
            obj1Field.setAccessible(true);
            Object object1Value=obj1Field.get(obj1);
            for (Field obj2Field : obj2Fields) {
                obj2Field.setAccessible(true);
                if(obj1Field.getName().equals(obj2Field.getName())){
                    Object object2Value=obj2Field.get(obj2);
                    //对于同一个字段，如果从自定义字段中读取到的不为空，从请求中拿到的为空,则把前者的值赋给后者，并返回后者
                    if(object1Value!=null && object2Value==null){
                        obj2Field.set(obj2,object1Value);
                    }
                    break;
                }
            }
        }
        return obj2;
    }
}
