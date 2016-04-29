package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.SiteConfig;
import org.springframework.stereotype.Component;

/**
 * <p>
 *     网站配置服务
 * </p>
 * @author xhl
 *
 * @since 1.2
 */
public interface SiteConfigService {
    /**
     * 根据站点信息获得站点配置信息对象
     *
     * @param site 站点信息对象
     * @return
     */
    SiteConfig findBySite(Site site);

    /**
     * 根据站点信息对象获得微官网域名
     *
     * @param site 站点信息对象
     * @return
     */
    String findMobileUrlBySite(Site site);
}
