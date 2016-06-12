package com.huotu.hotcms.admin.controller;

import com.huotu.hotcms.admin.annoation.AuthorizeRole;
import com.huotu.hotcms.admin.util.web.CookieUser;
import com.huotu.hotcms.service.common.EnumUtils;
import com.huotu.hotcms.service.common.SiteType;
import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Region;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.SiteConfig;
import com.huotu.hotcms.service.repository.RegionRepository;
import com.huotu.hotcms.service.repository.SiteConfigRepository;
import com.huotu.hotcms.service.service.HostService;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.service.util.PageData;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import com.huotu.hotcms.service.widget.service.StaticResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 超级管理员相关路由
 * </p>
 */
@Controller
@RequestMapping("/supper")
public class SupperController {
    private static final Log log = LogFactory.getLog(SupperController.class);

    @Autowired
    private SiteService siteService;

    @Autowired
    private HostService hostService;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private StaticResourceService resourceServer;

    @Autowired
    private CookieUser cookieUser;

    @Autowired
    private SiteConfigRepository siteConfigRepository;

    /**
     * 站点列表页面
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/siteList")
    @AuthorizeRole(roleType = AuthorizeRole.Role.Supper)
    public ModelAndView siteList(HttpServletRequest request) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/view/supper/siteList.html");
        return modelAndView;
    }

    /**
     * 添加站点页面
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addSite")
    @AuthorizeRole(roleType = AuthorizeRole.Role.Supper)
    public ModelAndView addSite(HttpServletRequest request) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/view/supper/addSite.html");
        List<Region> regions = regionRepository.findAll();
        modelAndView.addObject("regions", regions);
        modelAndView.addObject("siteTypes", SiteType.values());
        return modelAndView;
    }

    /**
     * 站点新增以及修改操作
     */
    @RequestMapping(value = "/saveSite", method = RequestMethod.POST)
    @Transactional(value = "transactionManager")
    @ResponseBody
    public ResultView updateSite(HttpServletRequest request, Site site, Long regionId, Integer siteType, Boolean personalise, String homeDomains, String... domains) {
        ResultView result = null;
        Set<Host> hosts = new HashSet<>();
        site.setPersonalise(personalise);
        Region region = regionRepository.findOne(regionId);
        site.setSiteType(EnumUtils.valueOf(SiteType.class, siteType));
        Site site2 = null;
        try {
            if (!cookieUser.isSupper(request)) {
                return new ResultView(ResultOptionEnum.NO_LIMITS.getCode(), ResultOptionEnum.NO_LIMITS.getValue(), null);
            }
            Long siteId = site.getSiteId();
            if (homeDomains == null) {
                return new ResultView(ResultOptionEnum.NOFIND_HOME_DEMON.getCode(), ResultOptionEnum.NOFIND_HOME_DEMON.getValue(), null);
            }
            if (siteId == null) {
                site.setCreateTime(LocalDateTime.now());
                site.setUpdateTime(LocalDateTime.now());
                site.setDeleted(false);
                result = hostService.addHost(domains, homeDomains, site, regionId);
            } else {//修改站点
                result = hostService.patchHost(domains, homeDomains, site, regionId);
            }
            if (result != null && result.getCode().equals(ResultOptionEnum.OK.getCode())) {
                site = (Site) result.getData();
                String resourceUrl = site.getResourceUrl();
                if (StringUtils.isEmpty(resourceUrl)) {
                    resourceUrl = resourceServer.getResource("").toString();
                }
                site.setResourceUrl(resourceUrl);
                if (siteId != null) {
                    site2 = siteService.getSite(site.getSiteId());
                    site = hostService.mergeSite(domains, site);
                    site.setUpdateTime(LocalDateTime.now());
                    hosts = hostService.getRemoveHost(domains, site2);
                }
                site.setRegion(region);
            } else {
                return result;
            }
            hostService.removeHost(hosts);
            siteService.save(site);
            result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            result = new ResultView(ResultOptionEnum.FAILE.getCode(), ResultOptionEnum.FAILE.getValue(), null);
        }
        return result;
    }

    /**
     * 修改站点页面
     *
     * @param id
     * @param customerId
     * @return
     * @throws Exception
     */
    @RequestMapping("/updateSite")
    @AuthorizeRole(roleType = AuthorizeRole.Role.Supper)
    public ModelAndView updateSite(@RequestParam(value = "id", defaultValue = "0") Long id, int customerId) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.setViewName("/view/supper/updateSite.html");
            String logo_uri = "";
            if (id != 0) {
                Site site = siteService.findBySiteIdAndCustomerId(id, customerId);
                if (site != null) {
                    if (!StringUtils.isEmpty(site.getLogoUri())) {
                        logo_uri = resourceServer.getResource(site.getLogoUri()).toString();
                    }
                    modelAndView.addObject("site", site);
                    modelAndView.addObject("logo_uri", logo_uri);
                    Set<Host> hosts = site.getHosts();
                    String domains = "";
                    for (Host host : hosts) {
                        String domain = host.getDomain();
                        domains = domains + domain + ",";
                    }
                    Region region = site.getRegion();
                    modelAndView.addObject("siteTypes", SiteType.values());
                    modelAndView.addObject("region", region);
                    modelAndView.addObject("homeDomain", hostService.getHomeDomain(site));
                    modelAndView.addObject("domains", domains.substring(0, domains.length() - 1));
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return modelAndView;
    }

    /**
     * 获取站点
     *
     * @param customerId
     * @param name
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/getSiteList")
    @ResponseBody
    public PageData<Site> getModelList(@RequestParam(name = "customerId", required = false) Integer customerId,
                                       @RequestParam(name = "name", required = false) String name,
                                       @RequestParam(name = "page", required = true, defaultValue = "1") int page,
                                       @RequestParam(name = "pagesize", required = true, defaultValue = "20") int pageSize) {
        PageData<Site> pageModel = null;
        try {
            pageModel = siteService.getPage(customerId, name, page, pageSize);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return pageModel;
    }

    /**
     * 删除站点(管理员权限)
     *
     * @param id
     * @param customerId
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteSite", method = RequestMethod.POST)
    @AuthorizeRole(roleType = AuthorizeRole.Role.Supper)
    @ResponseBody
    public ResultView deleteModel(@RequestParam(name = "id", required = true, defaultValue = "0") Long id, int customerId, HttpServletRequest request) {
        ResultView result = null;
        try {
            if (cookieUser.isSupper(request)) {
                Site site = siteService.findBySiteIdAndCustomerId(id, customerId);
                hostService.removeHost(site.getHosts());
                site.setHosts(null);
                site.setDeleted(true);
                siteService.save(site);
                result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
            } else {
                result = new ResultView(ResultOptionEnum.NO_LIMITS.getCode(), ResultOptionEnum.NO_LIMITS.getValue(), null);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            result = new ResultView(ResultOptionEnum.FAILE.getCode(), ResultOptionEnum.FAILE.getValue(), null);
        }
        return result;
    }

    @RequestMapping(value = "/siteConfig")
    @AuthorizeRole(roleType = AuthorizeRole.Role.Supper)
    public ModelAndView siteConfig(Long siteId,Integer customerId) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.setViewName("/view/supper/siteConfig.html");
            Site site=siteService.getSite(siteId);
            if(site!=null){
                modelAndView.addObject("siteConfig", siteConfigRepository.findBySite(site));
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/saveConfig")
    @AuthorizeRole(roleType = AuthorizeRole.Role.Supper)
    @ResponseBody
    public ResultView saveConfig(SiteConfig siteConfig,Long siteId){
        ResultView resultView=null;
        try{
            Site site=siteService.getSite(siteId);
            siteConfig.setSite(site);
            siteConfigRepository.save(siteConfig);
            resultView = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
        }catch (Exception ex){
            log.error(ex);
            resultView = new ResultView(ResultOptionEnum.SERVERFAILE.getCode(), ResultOptionEnum.SERVERFAILE.getValue(), null);
        }
        return resultView;
    }
}
