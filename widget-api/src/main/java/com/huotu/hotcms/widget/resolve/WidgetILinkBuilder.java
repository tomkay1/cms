package com.huotu.hotcms.widget.resolve;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.linkbuilder.AbstractLinkBuilder;
import org.thymeleaf.util.Validate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by lhx on 2016/8/26.
 */

public class WidgetILinkBuilder extends AbstractLinkBuilder {
    private static final Log log = LogFactory.getLog(WidgetILinkBuilder.class);

    public WidgetILinkBuilder() {
        super();
    }

    public String buildLink(IExpressionContext context, String base, Map<String, Object> parameters) {
        Validate.notNull(context, "Expression context cannot be null");
        if (base == null) {
            return null;
        }
        // We need to create a copy that is: 1. defensive, 2. mutable
        final Map<String, Object> linkParameters =
                (parameters == null || parameters.size() == 0 ? null : new LinkedHashMap<>(parameters));
        // id=1,name=2
        // c-id=1&c-name=2
        String componentId = (String) context.getVariable("componentId");
        Set<Map.Entry<String, Object>> entrys = linkParameters.entrySet();
        StringBuilder stringBuilder = new StringBuilder();
        if (parameters != null && parameters.size() > 0) {
            stringBuilder.append(base).append("?");
            for (Map.Entry entry : entrys) {
                stringBuilder.append(componentId).append("-")
                        .append(entry.getKey()).append("=")
                        .append(entry.getValue()).append("&");

            }
        } else
            stringBuilder.append(base);
        String url = stringBuilder.toString();
        url = url.substring(0, url.length() - 1);
        try {
            return URLEncoder.encode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
        return null;
    }


}
