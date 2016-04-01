package com.huotu.hotcms.service.widget.service;

import com.huotu.hotcms.service.model.widget.WidgetBase;
import com.huotu.hotcms.service.model.widget.WidgetProperty;
import com.huotu.hotcms.service.service.RedisService;
import com.huotu.hotcms.service.util.HttpUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/1.
 */
@Component
public class WidgetResolveService {
    private static final Log log = LogFactory.getLog(WidgetResolveService.class);

    @Autowired
    private StaticResourceService resourceServer;
    @Autowired
    private RedisService redisService;

    private TemplateEngine templateEngine = new TemplateEngine();

    public String widgetBriefView(String templateResources,Map<String,Object> map){
        Context context = new Context(Locale.CHINA, map);
        StringWriter writer = new StringWriter();
        templateEngine.process(templateResources, context, writer);
        return writer.toString();
    }

    public String widgetBriefView(String templateResources,WidgetBase widgetBase){
        if(widgetBase!=null) {
            Map<String, Object> map =null;
            if(widgetBase.getProperty()!=null){
                map = ConverMapByList(widgetBase.getProperty());
            }else{
                map=new HashMap<>();
            }
            Field[] fields = widgetBase.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!map.containsKey(field.getName())) {
                    map.put(field.getName(), getFieldValueByName(field.getName(), widgetBase));
                }
            }
            Context context = new Context(Locale.CHINA, map);
            StringWriter writer = new StringWriter();
            templateEngine.process(templateResources, context, writer);
            return writer.toString();
        }
        return templateResources;
    }

    public String widgetEditView(WidgetBase widgetBase){
        String templateResources=getWidgetEditTemplate(widgetBase);
        if(widgetBase!=null) {
            Map<String, Object> map =null;
            if(widgetBase.getProperty()!=null){
                map = ConverMapByList(widgetBase.getProperty());
            }else{
                map=new HashMap<>();
            }
            Field[] fields = widgetBase.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!map.containsKey(field.getName())) {
                    map.put(field.getName(), getFieldValueByName(field.getName(), widgetBase));
                }
            }
            Context context = new Context(Locale.CHINA, map);
            StringWriter writer = new StringWriter();
            templateEngine.process(templateResources, context, writer);
            return writer.toString();
        }
        return templateResources;
    }

    public String getWidgetEditTemplate(WidgetBase widgetBase){
        return isWidgetEditTemplateCached(widgetBase) ? getCachedWidgetEditTemplate(widgetBase) : getWidgetEditTemplateFromFile(widgetBase);
    }

    private boolean isWidgetEditTemplateCached(WidgetBase widgetBase) {
        return redisService.isWidgetEditExists(widgetBase.getId());
    }

    private String getCachedWidgetEditTemplate(WidgetBase widgetBase){
        return redisService.findByWidgetEditId(widgetBase.getId());
    }

    private String getWidgetEditTemplateFromFile(WidgetBase widgetBase){
        try {
            URL url = resourceServer.getResource(widgetBase.getWidgetEditUri()).toURL();
            String widgetTemplate = HttpUtils.getHtmlByUrl(url);
            redisService.saveWidgetEdit(widgetBase.getId(), widgetTemplate);
            return widgetTemplate;
        }catch(Exception ex){
            log.error("获得控件主体编辑视图失败");
            throw new ExceptionInInitializerError("获得控件主体编辑视图异常");
        }
    }

    private Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return null;
        }
    }

    private Map<String,Object> ConverMapByList(List<WidgetProperty> properties){
        Map<String,Object> objectMap=new HashMap<>();
        for(WidgetProperty widgetProperty:properties){
            objectMap.put(widgetProperty.getName(),widgetProperty.getValue());
        }
        return objectMap;
    }
}
