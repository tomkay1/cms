package com.huotu.hotcms.service.widget.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.hotcms.service.model.widget.WidgetBase;
import com.huotu.hotcms.service.model.widget.WidgetListProperty;
import com.huotu.hotcms.service.model.widget.WidgetProperty;
import com.huotu.hotcms.service.service.RedisService;
import com.huotu.hotcms.service.util.HttpUtils;
import com.huotu.hotcms.service.widget.model.GoodsCategory;
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
import java.util.*;

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

    @Autowired
    private GoodsCategoryService getGoodsCategories;

    private TemplateEngine templateEngine = new TemplateEngine();

    public String widgetBriefView(String templateResources,Map<String,Object> map){
        Context context = new Context(Locale.CHINA, map);
        StringWriter writer = new StringWriter();
        templateEngine.process(templateResources, context, writer);
        return writer.toString();
    }

    public String widgetBriefView(String templateResources,WidgetBase widgetBase) throws IOException {
        if(widgetBase!=null) {
            Map<String,Object> map =null;
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
            Context context=null;
//            Context context=new Context();
//            List<GoodsCategory> categories = getGoodsCategories.getGoodsCategories(4471);
//            map.put("categorys",categories);
            context = new Context(Locale.CHINA, map);
//            for ()
//            context.setVariable(map);
//            for (Map.Entry<String, Object> entry : map.entrySet()) {
//                context.setVariable(entry.getKey(),entry.getValue());
//            }
//            context.setVariable("categorys",categories);
            StringWriter writer = new StringWriter();
            templateEngine.process(templateResources, context, writer);
            return writer.toString();
        }
        return templateResources;
    }

    public String widgetEditView(WidgetBase widgetBase) throws IOException {
        String templateResources=getWidgetEditTemplate(widgetBase);
        if(widgetBase!=null) {
            Map map =null;
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

    private Map<String,Object> ConverMapByList(List<WidgetProperty> properties) throws IOException {
        Map<String,Object> objectMap=new HashMap<String,Object>();
        ObjectMapper objectMapper = new ObjectMapper();
        for(WidgetProperty widgetProperty:properties){
            Object obj=objectMapper.readValue(widgetProperty.getValue(),Object.class);
            objectMap.put(widgetProperty.getName(),obj);
        }
        return objectMap;
    }

    public static List<WidgetProperty> ConvertWidgetPropertyByMap(Map<String,Object> property) throws JsonProcessingException {
        List<WidgetProperty> list = new ArrayList<WidgetProperty>();
        ObjectMapper objectMapper = new ObjectMapper();
        for (Map.Entry<String, Object> entry : property.entrySet()) {
            WidgetProperty widgetProperty=new WidgetProperty();
            widgetProperty.setName(entry.getKey());
            String json=objectMapper.writeValueAsString(entry.getValue());
            widgetProperty.setValue(json);
            list.add(widgetProperty);
        }
        return list;
    }

//    public static WidgetListProperty<WidgetProperty> ConvertWidgetPropertyListByMap(Map<String,Object> property) throws JsonProcessingException {
//        WidgetListProperty<WidgetProperty> listProperty = new WidgetListProperty<WidgetProperty>();
//        List<WidgetProperty> list = new ArrayList<WidgetProperty>();
//        ObjectMapper objectMapper = new ObjectMapper();
//        for (Map.Entry<String, Object> entry : property.entrySet()) {
//            WidgetProperty widgetProperty=new WidgetProperty();
//            widgetProperty.setName(entry.getKey());
//            String json=objectMapper.writeValueAsString(entry.getValue());
//            widgetProperty.setValue(json);
//            list.add(widgetProperty);
//        }
//        listProperty.setList(list);
//        return listProperty;
//    }
//
//    public Map<String,Object> ConvertMapByWidgetListProperty(WidgetListProperty<WidgetProperty> widgetListProperty) throws IOException {
//        Map<String,Object> map=new HashMap<String,Object>();
//        List<WidgetProperty> widgetProperties=widgetListProperty.getList();
//        ObjectMapper objectMapper = new ObjectMapper();
//        for (WidgetProperty widgetProperty:widgetProperties){
//            Object obj=objectMapper.readValue(widgetProperty.getValue(),Object.class);
//            map.put(widgetProperty.getName(),obj);
//        }
//        return map;
//    }
}
