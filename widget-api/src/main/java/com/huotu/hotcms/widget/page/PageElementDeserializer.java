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

import java.io.IOException;

/**
 * Created by hzbc on 2016/6/23.
 */
public abstract class PageElementDeserializer<T> extends JsonDeserializer<T> {

    @Override
    public T deserialize(JsonParser p, DeserializationContext context) throws IOException {
        DeserializationConfig config = context.getConfig();
        SimpleType simpleType = SimpleType.construct(getImplementationClass());
        BeanDescription beanDesc = config.introspect(simpleType);
        BeanDeserializerFactory instance = new BeanDeserializerFactory(new DeserializerFactoryConfig());
        JsonDeserializer deserializer = instance.buildBeanDeserializer(context, simpleType, beanDesc);
        ((ResolvableDeserializer) deserializer).resolve(context);
        return (T) deserializer.deserialize(p, context);
    }

    public abstract Class<?> getImplementationClass();
}
