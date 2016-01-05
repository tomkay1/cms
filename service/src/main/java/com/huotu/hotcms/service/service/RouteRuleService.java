package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.RouteRule;
import com.huotu.hotcms.service.entity.Site;

import java.util.Set;

/**
 * <p>
 *     路由服务
 * </p>
 * @author xhl
 *
 * @since 1.0.0
 */
public interface RouteRuleService {
    Set<RouteRule> getRoutRule(Site site);

}
