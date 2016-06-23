package com.huotu.hotcms.widget.page;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenqi on 2016/6/23.
 */
public abstract class CMSDeserializer<T> extends JsonDeserializer<T> {

    @Override
    public T deserialize(JsonParser p, DeserializationContext context) throws IOException {
        DeserializationConfig config = context.getConfig();
        String nextFieldName=null;
        Class toParse=Layout.class;

        Field[] fields=Component.class.getDeclaredFields();
        List<String> fieldNames=new ArrayList<>();
        for (Field field:fields){
            fieldNames.add(field.getName());
        }

        while((nextFieldName=p.nextFieldName())!=null||p.nextTextValue()!=null){
            if(fieldNames.contains(nextFieldName)){
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
