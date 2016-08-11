/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package $

import java.io.IOException;{package};

import java.util.Locale;

import java.util.Locale;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetStyle;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * @author CJ
 */
public class WidgetInfo implements Widget{
    /*
     * 指定风格的模板类型 如：html,text等
     */
    public static final String VALID_STYLE_TEMPLATE = "styleTemplate";

    @Override
    public String groupId() {
        return "${groupId}";
    }

    @Override
    public String widgetId() {
        return "${artifactId}";
    }

    @Override
    public String name(Locale locale) {
        if (locale.equals(Locale.CHINA)) {
            return "${name}";
        }
        return "${artifactId}";
    }

    @Override
    public String description(Locale locale) {
        if (locale.equals(Locale.CHINESE)) {
            return "这是一个 ${name}，你可以对组件进行自定义修改。";
        }
        return "This is a ${artifactId},  you can make custom change the component.";
    }

    @Override
    public String dependVersion() {
        return "${apiVersion}";
    }

    @Override
    public WidgetStyle[] styles() {
        return new WidgetStyle[]{new DefaultWidgetStyle()};
    }

    @Override
    public Resource widgetDependencyContent(MediaType mediaType){
        if (mediaType.equals(Widget.Javascript))
            return new ClassPathResource("js/${artifactId}.js", getClass().getClassLoader());
        return null;
    }

    @Override
    public Map<String, Resource> publicResources() {
        Map<String, Resource> map = new HashMap<>();
        map.put("thumbnail/defaultStyleThumbnail.png",new ClassPathResource("thumbnail/defaultStyleThumbnail.png",getClass().getClassLoader()));
        return map;
    }

    @Override
    public void valid(String styleId, ComponentProperties componentProperties) throws IllegalArgumentException {
        WidgetStyle[] widgetStyles = styles();
        boolean flag = false;
        if (widgetStyles == null || widgetStyles.length < 1) {
            throw new IllegalArgumentException();
        }
        for (WidgetStyle ws : widgetStyles) {
            if ((flag = ws.id().equals(styleId))) {
                break;
            }
        }
        if (!flag) {
            throw new IllegalArgumentException();
        }
        //加入控件独有的属性验证

    }

    @Override
    public Class springConfigClass() {
        return null;
    }

    @Override
    public ComponentProperties defaultProperties(ResourceService resourceService) throws IOException {
        ComponentProperties properties = new ComponentProperties();
        return properties;
    }
}
