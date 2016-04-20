package com.huotu.hotcms.service.widget.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.widget.WidgetBase;
import com.huotu.hotcms.service.model.widget.WidgetProperty;
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
    private RequestService requestService;

    private TemplateEngine templateEngine = new TemplateEngine();

    private HttpServletRequest request = null;

    private void addDialect() {
        if (!templateEngine.isInitialized()) {
            templateEngine.addDialect(new WidgetDialect());
        }
    }

    private Context setVariable(Context context, Site site) {
        if (null != site) {
            context.setVariable("site", site);
        }
        context.setVariable("request", requestService.ConvertRequestModel(this.request,site));
        return context;
    }

    public String widgetBriefView(String templateResources, WidgetBase widgetBase, Site site) throws IOException {
        request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (widgetBase != null) {
            Map<String, Object> map = null;
            if (widgetBase.getProperty() != null) {
                map = ConvertMapByList(widgetBase.getProperty());
            } else {
                map = new HashMap<>();
            }
            Field[] fields = widgetBase.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!map.containsKey(field.getName())) {
                    map.put(field.getName(), ReflectionUtil.getFieldValueByName(field.getName(), widgetBase));
                }
            }
            Context context = new Context(Locale.CHINA, map);
            context = setVariable(context, site);
            addDialect();
            StringWriter writer = new StringWriter();
            templateEngine.process(templateResources, context, writer);
            return writer.toString();
        }
        return templateResources;
    }

    public String widgetEditView(WidgetBase widgetBase) throws IOException {
        String templateResources = getWidgetEditTemplate(widgetBase);
        if (widgetBase != null) {
            Map map = null;
            if (widgetBase.getProperty() != null) {
                map = ConvertMapByList(widgetBase.getProperty());
            } else {
                map = new HashMap<>();
            }
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

    public String getWidgetEditTemplate(WidgetBase widgetBase) {
        if (redisService.isContent())
            return isWidgetEditTemplateCached(widgetBase) ? getCachedWidgetEditTemplate(widgetBase) : getWidgetEditTemplateFromFile(widgetBase);
        else
            return getWidgetEditTemplateFromFile(widgetBase);
    }

    private boolean isWidgetEditTemplateCached(WidgetBase widgetBase) {
        return redisService.isWidgetEditExists(widgetBase.getId());
    }

    private String getCachedWidgetEditTemplate(WidgetBase widgetBase) {
        return redisService.findByWidgetEditId(widgetBase.getId());
    }

    private String getWidgetEditTemplateFromFile(WidgetBase widgetBase) {
        try {
            URL url = resourceServer.getResource(widgetBase.getWidgetEditUri()).toURL();
            String widgetTemplate = HttpUtils.getHtmlByUrl(url);
            if(redisService.isContent()){
                redisService.saveWidgetEdit(widgetBase.getId(), widgetTemplate);
            }
            return widgetTemplate;
        } catch (Exception ex) {
            log.error("获得控件主体编辑视图失败");
            throw new ExceptionInInitializerError("获得控件主体编辑视图异常");
        }
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
}
