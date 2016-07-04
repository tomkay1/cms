/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.filter;

import com.huotu.hotcms.service.FilterBehavioral;
import com.huotu.hotcms.service.entity.Site;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 有时候站点会定义转发
 *
 * @author CJ
 */
@Component
public class SiteRedirectFilter implements FilterBehavioral {
    @Override
    public boolean doSiteFilter(Site site, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (site.getRedirectUrl() != null) {
            response.sendRedirect(site.getRedirectUrl());
            return false;
        }
        return true;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
