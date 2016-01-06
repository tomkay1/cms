package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.RouteRule;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.RouteRuleRepository;
import com.huotu.hotcms.service.service.RouteRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * <p>
 *     路由服务
 * </p>
 * @author xhl
 *
 * @since 1.0.0
 */
@Service
public class RouteRuleServiceImpl implements RouteRuleService {

    @Autowired
    private RouteRuleRepository routRuleRepository;

    @Override
    public Set<RouteRule> getRoutRule(Site site) {
        return routRuleRepository.findBySite(site);
    }
}
