/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget;

import com.huotu.hotcms.service.entity.AbstractContent;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.resolve.WidgetConfiguration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

/**
 * CMS上下文,或者称之为交互空间
 * <p>
 * 每一个CMS线程都应当有拥有一个交互空间
 * </p>
 *
 * @author CJ
 */
@Setter
@Getter
@ToString
@RequiredArgsConstructor
public class CMSContext {

    private static final Log log = LogFactory.getLog(CMSContext.class);

    //    /**
//     * CMS站点所有者Id,必选
//     */
//    private long ownerId;
//    /**
//     * 商户id,可选
//     */
//    private Long merchantId;
    private final static ThreadLocal<CMSContext> contexts = new ThreadLocal<>();
    /**
     * 内部使用
     */
    private final Stack<WidgetConfiguration> widgetConfigurationStack = new Stack<>();
    /**
     * spring上下文
     */
    private final WebApplicationContext webApplicationContext;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private Site site;
    private Locale locale;
    /**
     * layout列比值
     */
    private Integer column = null;

    /**
     * 如果当前展示的页面类型为{@link com.huotu.hotcms.service.common.PageType#DataContent}则必须提供一个当前内容
     */
    private AbstractContent abstractContent;
    /**
     * 当前正在展示的页面
     */
    private PageInfo currentPage;
    /**
     * 当前页面控件参数列表
     */
    private Map<String, Map<String, String>> parameters = new HashMap<>();

    /**
     * 请求当前的CMS上下文
     *
     * @return 上下文
     */
    public static CMSContext RequestContext() {
        CMSContext cmsContext = contexts.get();
        if (cmsContext == null) {
            log.error("NO CMSContext Stored!!");
            throw new IllegalStateException("StoreContext before RequestContext! Please Use CMSFilter!");
        }
        return cmsContext;
    }


    /**
     * 更新当前CMS上下文
     *
     * @param request  request
     * @param response response
     * @param site     当前站点
     */
    public static CMSContext PutContext(HttpServletRequest request, HttpServletResponse response, Site site) {
        CMSContext cmsContext = new CMSContext(WebApplicationContextUtils.findWebApplicationContext(request.getServletContext())
                , request, response);
        cmsContext.updateSite(site);
        contexts.set(cmsContext);
        return cmsContext;
    }

    public void updateSite(Site site) {
        setSite(site);
        setLocale(site.getRegion() == null ? request.getLocale()
                : site.getRegion().getLocale());
    }

    /**
     * 设置当前CMS列值
     *
     * @param column 可以为null
     */
    public void updateNextBootstrapLayoutColumn(Integer column) {
        this.column = column;
    }

    /**
     * 获取当前CMS列值
     *
     * @return bootstrap classname
     */
    public String getNextBootstrapClass() {
        CMSContext cmsContext = RequestContext();
        return "col-md-" + cmsContext.column;
    }


    public RequestContext getRequestContext() {
        return new RequestContext(request, response, request.getServletContext(), null);
    }

    public void widgetContextVariables(Map<String, Object> variables) {

    }


    /**
     * 获取这个组件的请求参数
     *
     * @param component 组件,可能为null
     * @return 可以为null
     */
    public Map<String, String> getWidgetParameters(Component component) {
        if (component == null)
            return null;
        else {
            return parameters.get(component.getId());
        }
    }
}
