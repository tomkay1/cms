package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.RoutRuled;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.RoutRuleRepository;
import com.huotu.hotcms.service.service.RoutRuleService;
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
public class RoutRuleServiceImpl implements RoutRuleService{

    @Autowired
    private RoutRuleRepository routRuleRepository;

    @Override
    public Set<RoutRuled> getRoutRule(Site site) {
        return routRuleRepository.findBySite(site);
    }
}
