package com.huotu.hotcms.web.service;
import com.huotu.hotcms.service.entity.RoutRuled;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.service.HostService;
import com.huotu.hotcms.service.service.RegionService;
import com.huotu.hotcms.service.service.RoutRuleService;
import com.huotu.hotcms.service.service.impl.RoutRuleServiceImpl;
import com.huotu.hotcms.web.util.PatternMatchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * <p>
 *     路由规则解析服务
 * </p>
 * @author xhl
 *
 * @since 1.0.0
 */
@Component
public class RoutResolverService {

    @Autowired
    private RoutRuleService routRuleService;

    /**
     * 根据url和站点信息获得路由规则
     *
     * @param site
     * @param url
     * @return
     * */
    public RoutRuled getRout(Site site,String url) {
        RoutRuled routRuled=null;
        Set<RoutRuled> ruleds =routRuleService.getRoutRule(site);
        for(RoutRuled s : ruleds) {
            if(PatternMatchUtil.match(url,s.getRule().toString()))
            {
                routRuled=s;
            }
        }
        return routRuled;
    }


}
