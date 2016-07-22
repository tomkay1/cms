/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.web.interceptor;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.thymeleaf.service.SiteResolveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Component
public class SiteResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private SiteResolveService siteResolveService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == Site.class;
    }

    /**
     * 根据域名及语言偏好获得站点信息
     * <p>
     * 请求路径
     * 场景A： www.mi.com
     * 场景B： www.mi.com/cn
     * 获取语言偏好
     * 场景A：获取浏览器语言偏好，如没有偏好，则设置为中文
     * 场景B：从数据库获得,没有则抛异常
     * 根据域名查找对应站点列表
     * 匹配站点：
     * 场景A：并且没有符合的站点，自动路由到中文站
     * 场景B：如果是指定区域,并且没有符合的站点，则抛异常
     *
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer
            , NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        // 应该直接使用CMSContext 更好么?
        return siteResolveService.getCurrentSite(request);
    }


}
