
/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.page;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.huotu.hotcms.widget.Component;

import java.io.IOException;
import java.util.Iterator;

/**
 *  用于本系统对接口{@link com.huotu.hotcms.widget.page.PageElement}的反序列化
 * @param <T> 接口
 */
public abstract class CMSDeserializer<T> extends JsonDeserializer<T> {

    @Override
    public T deserialize(JsonParser p, DeserializationContext context) throws IOException {
        DeserializationConfig config = context.getConfig();
        Class toParse=Layout.class;
        Iterator<String> iterator= p.readValueAsTree().fieldNames();
        while(iterator.hasNext()){
            if(iterator.next().equals("widgetIdentity")){
                toParse= Component.class;
                break;
            }
        }
        SimpleType simpleType = SimpleType.construct(toParse);
        BeanDescription beanDesc = config.introspect(simpleType);
         BeanDeserializerFactory instance = new BeanDeserializerFactory(new DeserializerFactoryConfig());
        JsonDeserializer deserializer = instance.buildBeanDeserializer(context, simpleType, beanDesc);
        ((ResolvableDeserializer) deserializer).resolve(context);
        return (T) deserializer.deserialize(p, context);
    }
}
