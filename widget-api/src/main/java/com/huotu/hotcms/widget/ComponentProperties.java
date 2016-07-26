/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 组件属性
 *
 * @author CJ
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public class ComponentProperties extends HashMap<String, Object> {

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof Map))
            return false;
        Map<?, ?> m = (Map<?, ?>) o;
        if (m.size() != size())
            return false;

        try {
            Iterator<Entry<String, Object>> i = entrySet().iterator();
            while (i.hasNext()) {
                Entry<String, Object> e = i.next();
                String key = e.getKey();
                Object value = e.getValue();
                if (value == null) {
                    if (!(m.get(key) == null && m.containsKey(key)))
                        return false;
                } else {
                    if (isArray(value)) {
                        if (!isArray(m.get(key)))
                            return false;
                        Object[] values = toObjectArray(value);

                        if (!Arrays.equals(values, toObjectArray(m.get(key))))
                            return false;
                    } else if (!value.equals(m.get(key)))
                        return false;
                }
            }
        } catch (ClassCastException unused) {
            return false;
        } catch (NullPointerException unused) {
            return false;
        }

        return true;
    }

    private Object[] toObjectArray(Object value) {
        if (value instanceof Collection) {
            Collection collection = (Collection) value;
            return collection.toArray(new Object[collection.size()]);
        }
        return (Object[]) value;
    }

    private boolean isArray(Object value) {
        return value != null && (value instanceof Collection || value.getClass().isArray());
    }
}
