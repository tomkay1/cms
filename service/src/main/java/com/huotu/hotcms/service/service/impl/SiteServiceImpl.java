/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.AbstractContent;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Region;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.event.DeleteSiteEvent;
import com.huotu.hotcms.service.exception.NoSiteFoundException;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.HostRepository;
import com.huotu.hotcms.service.repository.RegionRepository;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.ContentService;
import com.huotu.hotcms.service.service.HostService;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.service.util.SerialUtil;
import com.huotu.hotcms.service.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Locale;
import java.util.Set;

/**
 * Created by chendeyu on 2015/12/24.
 */
@Service
public class SiteServiceImpl implements SiteService {

    private static final Log log = LogFactory.getLog(SiteServiceImpl.class);
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private HostRepository hostRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private HostService hostService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ContentService contentService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;

    @Override
    public void deleteData(Site site) throws IOException {
        applicationEventPublisher.publishEvent(new DeleteSiteEvent(site));

        for (AbstractContent content : contentService.listBySite(site, null)) {
            contentService.delete(content);
        }

        for (Category category : categoryRepository.findBySite(site)) {
            if (categoryRepository.exists(category.getId()))
                categoryService.delete(category);
        }
    }

    @Override
    public Site closestSite(Host host, Locale locale) throws NoSiteFoundException {
        if (host.getSites() == null || host.getSites().isEmpty())
            throw new NoSiteFoundException("no site here");
        if (host.getSites().size() == 1)
            return host.getSites().values().iterator().next();
        //逐个检查语言环境
        Locale originLocale = locale;

        while (locale != null) {
            Region region = regionRepository.findOne(locale);
            if (region == null) {
                //降级
                locale = downGrade(locale);
                continue;
            }
            Site site = host.getSites().get(region);
            if (site != null)
                return site;
            locale = downGrade(locale);
        }
        // 实在没有找到
        log.warn("no site found for host:" + host + " locale:" + originLocale);
        return host.getSites().values().iterator().next();
    }

    private Locale downGrade(Locale locale) {
        if (locale.getVariant().length() > 0)
            return new Locale(locale.getLanguage(), locale.getCountry());
        if (locale.getCountry().length() > 0)
            return new Locale(locale.getLanguage());
        return null;
    }

    @Override
    public Site getSite(long siteId) {
        return siteRepository.findOne(siteId);
    }

    @Override
    public Set<Site> findByOwnerIdAndDeleted(long ownerId, boolean deleted) {
        return siteRepository.findByOwner_IdAndDeleted(ownerId, deleted);
    }


    //    @Override
//    public void siteCopy(long templateId, Site customerSite) throws Exception {
//        //根据模板ID读取到相应的站点
//        Template template = templateRepository.findOne(templateId);
////        Site templateSite = template.getSite();
//        List<CustomPages> customPages = customPagesRepository.findBySite(template);
//        for (CustomPages customPage : customPages) {
//            String templateResourceConfigUrl = configInfo.getResourcesConfig(template) + "/" + customPage.getId() + ".xml";
//            URI url = resourceService.getResource(templateResourceConfigUrl).httpUrl().toURI();
//            InputStream inputStream = HttpUtils.getInputStreamByUrl(url.toURL());
//            WidgetPage widgetPage = JAXB.unmarshal(inputStream, WidgetPage.class);
//            pageResolveService.createPageAndConfigByWidgetPage(widgetPage,
//                    customerSite.getSiteId(), false);
//        }
//        createDefaultWidgetPage(template, customerSite);
//        deepCopy(template, customerSite);
//    }
    @Override
    public Site newSite(String[] domains, String homeDomains, Site site, Locale locale) {
        if (!StringUtil.Contains(domains, homeDomains)) {//不存在主推域名则外抛出
            // 这是一个很糟糕的设计
            throw new IllegalArgumentException("没有主推域名");
//            return new ResultView(ResultOptionEnum.NOFIND_HOME_DEMON.getCode(), ResultOptionEnum.NOFIND_HOME_DEMON.getValue(), null);
        }
        Region region = regionRepository.findOne(locale);
        if (region == null) {
            region = new Region();
            region.setLocale(locale);
            region = regionRepository.save(region);
        }

        site.setRegion(region);
        site = siteRepository.save(site);

        //检查域名,如果是新的域名 则直接添加,如果已经存在域名则需要检查Owner
        for (String domain : domains) {
            Host host = hostService.getHost(domain);
            if (host == null) {
                host = new Host();
                host.setDomain(domain);
                host.setOwner(site.getOwner());
                host.setSerial(SerialUtil.formatSerial(site));
//                if (domain.equals(homeDomains))
//                    host.setHome(true);
                host.addSite(site);
                hostRepository.save(host);
            } else {
                // host 已存在
                if (host.getOwner() != null && !host.getOwner().equals(site.getOwner())) {
                    throw new IllegalArgumentException("域名已经存在");
//                    return new ResultView(ResultOptionEnum.DOMAIN_EXIST.getCode(), ResultOptionEnum.DOMAIN_EXIST.getValue(), null);
                }
                host.addSite(site);
                hostRepository.saveAndFlush(host);
            }
        }
        site.setRecommendDomain(homeDomains);
        siteRepository.save(site);
//        return new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), site);
        return site;
    }

    @Override
    public Site patchSite(String[] domains, String homeDomains, Site site, Locale locale) {
        // 将之前关联
        hostService.stopHookSite(site);
        return newSite(domains, homeDomains, site, locale);
    }
}
