package com.huotu.hotcms.widget.resolve;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.linkbuilder.AbstractLinkBuilder;
import org.thymeleaf.util.Validate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    private static int findCharInSequence(final CharSequence seq, final char character) {
        int n = seq.length();
        while (n-- != 0) {
            final char c = seq.charAt(n);
            if (c == character) {
                return n;
            }
        }
        return -1;
    }

    private static boolean isLinkBaseContextRelative(final CharSequence linkBase) {
        if (linkBase.length() == 0 || linkBase.charAt(0) != '/') {
            return false;
        }
        return linkBase.length() == 1 || linkBase.charAt(1) != '/';
    }

    private static boolean isLinkBaseAbsolute(final CharSequence linkBase) {
        final int linkBaseLen = linkBase.length();
        if (linkBaseLen < 2) {
            return false;
        }
        final char c0 = linkBase.charAt(0);
        if (c0 == 'm' || c0 == 'M') {
            // Let's check for "mailto:"
            if (linkBase.length() >= 7 &&
                    Character.toLowerCase(linkBase.charAt(1)) == 'a' &&
                    Character.toLowerCase(linkBase.charAt(2)) == 'i' &&
                    Character.toLowerCase(linkBase.charAt(3)) == 'l' &&
                    Character.toLowerCase(linkBase.charAt(4)) == 't' &&
                    Character.toLowerCase(linkBase.charAt(5)) == 'o' &&
                    Character.toLowerCase(linkBase.charAt(6)) == ':') {
                return true;
            }
        } else if (c0 == '/' || c0 == '/') {
            return linkBase.charAt(1) == '/'; // It starts with '//' -> true, any other '/x' -> false
        }
        for (int i = 0; i < (linkBaseLen - 2); i++) {
            // Let's try to find the '://' sequence anywhere in the base --> true
            if (linkBase.charAt(i) == ':' && linkBase.charAt(i + 1) == '/' && linkBase.charAt(i + 2) == '/') {
                return true;
            }
        }
        return false;
    }

    private static boolean isLinkBaseServerRelative(final CharSequence linkBase) {
        // For this to be true, it should start with '~/'
        return (linkBase.length() >= 2 && linkBase.charAt(0) == '~' && linkBase.charAt(1) == '/');
    }

    public String buildLink(IExpressionContext context, String base, Map<String, Object> parameters) {
        Validate.notNull(context, "Expression context cannot be null");
        if (base == null) {
            return null;
        }
        // We need to create a copy that is: 1. defensive, 2. mutable
        final Map<String, Object> linkParameters =
                (parameters == null || parameters.size() == 0 ? null : new LinkedHashMap<>(parameters));

        final LinkType linkType;
        if (isLinkBaseAbsolute(base)) {
            linkType = LinkType.ABSOLUTE;
        } else if (isLinkBaseContextRelative(base)) {
            linkType = LinkType.CONTEXT_RELATIVE;
        } else if (isLinkBaseServerRelative(base)) {
            linkType = LinkType.SERVER_RELATIVE;
        } else {
            linkType = LinkType.BASE_RELATIVE;
        }

        final int hashPosition = findCharInSequence(base, '#');

        final String contextPath =
                (linkType == LinkType.CONTEXT_RELATIVE ? computeContextPath(context, base, parameters) : null);
        final boolean contextPathEmpty = contextPath == null || contextPath.length() == 0 || contextPath.equals("/");
        if (contextPathEmpty && linkType != LinkType.SERVER_RELATIVE &&
                (linkParameters == null || linkParameters.size() == 0) && hashPosition < 0) {
            return processLink(context, base);
        }

        // c-id=1&c-name=2
        String componentId = (String) context.getVariable("componentId");
        StringBuilder stringBuilder = new StringBuilder();
        if (parameters != null && parameters.size() > 0) {
            stringBuilder.append(contextPath);
            stringBuilder.append(base).append("?");
            Set<Map.Entry<String, Object>> entrys = linkParameters.entrySet();
            for (Map.Entry entry : entrys) {
                stringBuilder.append(componentId).append("-")
                        .append(entry.getKey()).append("=")
                        .append(entry.getValue()).append("&");

            }
        } else {
            stringBuilder.append(contextPath);
            stringBuilder.append(base);
        }

        String url = stringBuilder.toString();
        url = url.substring(0, url.length() - 1);
        try {
            return URLEncoder.encode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * <p>
     * Compute the context path to be applied to URLs that have been determined to be context-relative (and therefore
     * need a context path to be inserted at their beginning).
     * </p>
     * <p>
     * By default, this method will obtain the context path from <tt>HttpServletRequest.getContextPath()</tt>,
     * throwing an exception if <tt>context</tt> is not an instance of <tt>IWebContext</tt> given context-relative
     * URLs are (by default) only allowed in web contexts.
     * </p>
     * <p>
     * This method can be overridden by any subclasses that want to change this behaviour (e.g. in order to
     * avoid using the Servlet API for resolving context path or to allow context-relative URLs in non-web
     * contexts).
     * </p>
     *
     * @param context    the execution context.
     * @param base       the URL base specified.
     * @param parameters the URL parameters specified.
     * @return the context path.
     */
    protected String computeContextPath(
            final IExpressionContext context, final String base, final Map<String, Object> parameters) {

        if (!(context instanceof IWebContext)) {
            throw new TemplateProcessingException(
                    "Link base \"" + base + "\" cannot be context relative (/...) unless the context " +
                            "used for executing the engine implements the " + IWebContext.class.getName() + " interface");
        }
        // If it is context-relative, it has to be a web context
        final HttpServletRequest request = ((IWebContext) context).getRequest();
        return request.getContextPath();

    }

    /**
     * <p>
     * Process an already-built URL just before returning it.
     * </p>
     * <p>
     * By default, this method will apply the <tt>HttpServletResponse.encodeURL(url)</tt> mechanism, as standard
     * when using the Java Servlet API. Note however that this will only be applied if <tt>context</tt> is
     * an implementation of <tt>IWebContext</tt> (i.e. the Servlet API will only be applied in web environments).
     * </p>
     * <p>
     * This method can be overridden by any subclasses that want to change this behaviour (e.g. in order to
     * avoid using the Servlet API).
     * </p>
     *
     * @param context the execution context.
     * @param link    the already-built URL.
     * @return the processed URL, ready to be used.
     */
    protected String processLink(final IExpressionContext context, final String link) {
        if (!(context instanceof IWebContext)) {
            return link;
        }
        final HttpServletResponse response = ((IWebContext) context).getResponse();
        return (response != null ? response.encodeURL(link) : link);

    }


    protected enum LinkType {ABSOLUTE, CONTEXT_RELATIVE, SERVER_RELATIVE, BASE_RELATIVE}


}
