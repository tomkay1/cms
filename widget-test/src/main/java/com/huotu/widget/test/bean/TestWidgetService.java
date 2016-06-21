package com.huotu.widget.test.bean;

import com.huotu.hotcms.widget.*;
import com.huotu.widget.test.WidgetTestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.spring4.SpringTemplateEngine;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Map;

/**
 * Created by lhx on 2016/6/21.
 */
@Service
public class TestWidgetService implements WidgetService {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    WidgetHolder widgetHolder;


    @Override
    public URI resourceURI(Widget widget, String resourceName) {
        Map<String ,Resource> publicResources = widget.publicResources();
        Resource resource = publicResources.get(resourceName);
        try {
            return resource.getURI();
        } catch (IOException e) {
        }
        return null;
    }

    @Override
    public String previewHTML(Widget widget, String styleId, CMSContext cmsContext, ComponentProperties properties,WebEngineContext context) {
        SpringTemplateEngine engine = applicationContext.getBean(SpringTemplateEngine.class);
        String finalHTML = engine.process("TEMPLATE/" +  WidgetTestConfig.WidgetIdentity((Widget) context.getVariable("widget"))
                + "/" + styleId , Collections.singleton("div"), context);
        return finalHTML;
    }

    @Override
    public String editorHTML(Widget widget, CMSContext cmsContext,WebEngineContext context) {
        SpringTemplateEngine engine = applicationContext.getBean(SpringTemplateEngine.class);
        String finalHTML = engine.process("EDITOR/"+ WidgetTestConfig.WidgetIdentity((Widget) context.getVariable("widget"))
                , Collections.singleton("div"), context);
        return finalHTML;
    }

    @Override
    public String componentHTML(Component component, CMSContext cmsContext,WebEngineContext context) {
        return null;
    }
}
