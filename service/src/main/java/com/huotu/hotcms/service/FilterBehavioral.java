/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service;

import com.huotu.hotcms.service.entity.Site;
import org.springframework.core.Ordered;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器的行为,
 * 按照Servlet Filter的一般处理,过滤器大致的几个过滤行为
 *
 * @author CJ
 */
public interface FilterBehavioral extends Ordered {

    /**
     * 执行过滤
     *
     * @param site     当前的站点
     * @param request  请求实例
     * @param response 响应实例
     * @return 查看 {@link FilterStatus}即可
     * @throws IOException 因为异常而需要停止反应时
     */
    FilterStatus doSiteFilter(Site site, HttpServletRequest request, HttpServletResponse response) throws IOException;

    enum FilterStatus {
        /**
         * 继续走{@link javax.servlet.FilterChain#doFilter(ServletRequest, ServletResponse)}
         */
        CHAIN,
        /**
         * 停止一切响应
         */
        STOP,
        /**
         * 继续其他{@link FilterBehavioral}
         */
        NEXT
    }

}
