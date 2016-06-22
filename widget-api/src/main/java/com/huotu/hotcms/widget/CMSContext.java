/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget;

import com.huotu.hotcms.widget.resolve.WidgetConfiguration;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
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
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Locale locale;

    private CMSContext() {
    }

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
     * @param request
     * @param response
     */
    public static CMSContext PutContext(HttpServletRequest request, HttpServletResponse response) {
        CMSContext cmsContext = new CMSContext();
        cmsContext.request = request;
        cmsContext.response = response;
        cmsContext.locale = request.getLocale();
        contexts.set(cmsContext);
        return cmsContext;
    }
}
