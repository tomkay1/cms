package com.huotu.hotcms.service.widget.service;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.WidgetMains;
import com.huotu.hotcms.service.model.widget.WidgetBase;
import com.huotu.hotcms.service.model.widget.WidgetPage;
import com.huotu.hotcms.service.model.widget.WidgetProperty;
import com.huotu.hotcms.service.repository.WidgetMainsRepository;
import com.huotu.hotcms.service.service.RedisService;
import com.huotu.hotcms.service.thymeleaf.dialect.WidgetDialect;
import com.huotu.hotcms.service.thymeleaf.service.RequestService;
import com.huotu.hotcms.service.util.HttpUtils;
import com.huotu.hotcms.service.util.ReflectionUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;

/**
 * <p>
 *     组件解析服务
 * </p>
 * @since 1.2
 *
 * @author xhl
 */
@Component
public class WidgetResolveService {
    private static final Log log = LogFactory.getLog(WidgetResolveService.class);

    @Autowired
    private RequestService requestService;

    @Autowired
    private WidgetResourceService widgetResourceService;

    @Autowired
    private WidgetDialect widgetDialect;

    @Autowired
    private WidgetMainsRepository widgetMainsRepository;

    private TemplateEngine templateEngine = new TemplateEngine();

    private HttpServletRequest request = null;

    /**
     * <p>添加方言</p>
     * */
    private void addDialect() {
        if (!templateEngine.isInitialized()) {
            templateEngine.addDialect(widgetDialect);
        }
    }

    /**
     * <p>设置Context所需要的扩展内置对象</p>
     * */
    public Context setVariable(Context context, Site site,WidgetPage widgetPage) {
        if (null != site) {
            context.setVariable("site", site);
        }
        if(null!=widgetPage){
            context.setVariable("widgetPage",widgetPage);
        }
        context.setVariable("request", requestService.ConvertRequestModel(this.request,site));
        return context;
    }

    /**
     * <p>获得控件主体预览视图</p>
     * @param templateResources 模版资源
     * @param widgetBase 控件主体对象
     * @param site 站点信息
     * @return 返回控件主体预览视图
     * */
    public String widgetBriefView(String templateResources, WidgetBase widgetBase, Site site) throws IOException {
        request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (widgetBase != null) {
            Map<String, Object> map = null;
            map = getWidgetMainsDefaults(widgetBase);
//            if (widgetBase.getProperty() != null) {
//                map = ConvertMapByList(widgetBase.getProperty());
//            } else {
//                map=getWidgetMainsDefaults(widgetBase);
//            }
            Field[] fields = widgetBase.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!map.containsKey(field.getName())) {
                    map.put(field.getName(), ReflectionUtil.getFieldValueByName(field.getName(), widgetBase));
                }
            }
            Context context = new Context(Locale.CHINA, map);
            context = setVariable(context, site,null);
            addDialect();
            StringWriter writer = new StringWriter();
            templateEngine.process(templateResources, context, writer);
            return writer.toString();
        }
        return templateResources;
    }

    /**
     * <p>
     *     获得控件主体编辑视图
     * </p>
     * @param widgetBase 控件主体对象
     * @return 返回控件主体编辑视图
     * */
    public String widgetEditView(WidgetBase widgetBase) throws IOException {
        String templateResources = widgetResourceService.getWidgetEditTemplate(widgetBase);
        if (widgetBase != null) {
            Map map = null;
            map = getWidgetMainsDefaults(widgetBase);
//            if (widgetBase.getProperty() != null) {
//                map = ConvertMapByList(widgetBase.getProperty());
//            } else {
//                map=getWidgetMainsDefaults(widgetBase.getId());
//            }
            Field[] fields = widgetBase.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!map.containsKey(field.getName())) {
                    map.put(field.getName(), ReflectionUtil.getFieldValueByName(field.getName(), widgetBase));
                }
            }
            Context context = new Context(Locale.CHINA, map);
            addDialect();
            StringWriter writer = new StringWriter();
            templateEngine.process(templateResources, context, writer);
            return writer.toString();
        }
        return templateResources;
    }

    private Map<String, Object> ConvertMapByList(List<WidgetProperty> properties) throws IOException {
        Map<String, Object> objectMap = new HashMap<String, Object>();
        ObjectMapper objectMapper = new ObjectMapper();
        for (WidgetProperty widgetProperty : properties) {
            Object obj = objectMapper.readValue(widgetProperty.getValue(), Object.class);
            objectMap.put(widgetProperty.getName(), obj);
        }
        return objectMap;
    }

    public static List<WidgetProperty> ConvertWidgetPropertyByMap(Map<String, Object> property) throws JsonProcessingException {
        List<WidgetProperty> list = new ArrayList<WidgetProperty>();
        ObjectMapper objectMapper = new ObjectMapper();
        for (Map.Entry<String, Object> entry : property.entrySet()) {
            WidgetProperty widgetProperty = new WidgetProperty();
            widgetProperty.setName(entry.getKey());
            String json = objectMapper.writeValueAsString(entry.getValue());
            widgetProperty.setValue(json);
            list.add(widgetProperty);
        }
        return list;
    }

    /**
     * <p>
     *     根据控件主体Id获得默认属性,不存在的属性则以默认属性为主
     * </p>
     * @param widgetBase 控件主体ID
     * @return 默认属性Map字典
     * */
    public Map<String,Object> getWidgetMainsDefaults(WidgetBase widgetBase){
        Map<String,Object> map=new HashMap<>();
        try{
            WidgetMains widgetMains=widgetMainsRepository.findOne(widgetBase.getId());
            List<WidgetProperty> defaultPropertyList=null;
            if(widgetMains!=null){
                String defaultProperty=widgetMains.getDefaultsProperty();
                if(defaultProperty!=null&&defaultProperty!=""){
                    defaultPropertyList= JSONArray.parseArray(defaultProperty, WidgetProperty.class);
                }
            }
            List<WidgetProperty> widgetProperties=widgetBase.getProperty();
            List<WidgetProperty> widgetProperties1=null;
            if(widgetProperties!=null&&defaultPropertyList!=null){
                widgetProperties1=mergeWidgetProperty(widgetProperties,defaultPropertyList);
            }
            if(widgetProperties==null&&defaultPropertyList!=null){
                widgetProperties1=defaultPropertyList;
            }
            if(defaultPropertyList==null&&widgetProperties!=null){
                widgetProperties1=widgetProperties;
            }
            map=ConvertMapByList(widgetProperties1);
        }catch (Exception ex){
            ex.getStackTrace();
        }
        return map;
    }

    /**
     * <p>
     *     合并属性列表对象,设置属性列表中没有则取控件主体默认的属性值
     * </p>
     * @param widgetProperties 控件主体设置信息
     * @param defaultsProperty 控件主体默认设置信息
     * @return 控件主体设置信息列表
     * */
    private List<WidgetProperty> mergeWidgetProperty(List<WidgetProperty> widgetProperties,List<WidgetProperty> defaultsProperty){
        if(defaultsProperty!=null&&defaultsProperty.size()>0){
            List<WidgetProperty> widgetPropertyList=new ArrayList<>();
            for(WidgetProperty widgetProperty:defaultsProperty){
                WidgetProperty widgetProperty1=getDefaultValue(widgetProperties,widgetProperty.getName());
                if(widgetProperty1==null){
                    widgetPropertyList.add(widgetProperty);
                }else{
                    widgetPropertyList.add(widgetProperty1);
                }
            }
            for(WidgetProperty widgetProperty:widgetProperties){
                if(!existsWidgetProperty(widgetPropertyList,widgetProperty.getName())){
                    widgetPropertyList.add(widgetProperty);
                }
            }
            return widgetPropertyList;
        }else{
            return widgetProperties;
        }
    }

    private Boolean existsWidgetProperty(List<WidgetProperty> widgetProperties,String name){
        for(WidgetProperty widgetProperty:widgetProperties){
            if(widgetProperty.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    /**
     * <p>
     * 根据属性名称获得默认值
     * </p>
     * @param widgetProperties 目标属性列表
     * @param name 名称
     * */
    private WidgetProperty getDefaultValue(List<WidgetProperty> widgetProperties,String name){
        for(WidgetProperty widgetProperty:widgetProperties){
            if(name.equals(widgetProperty.getName())){
                return widgetProperty;
            }
        }
        return null;
    }

}
