package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.util.web.CookieUser;
import com.huotu.hotcms.service.common.SiteType;
import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Region;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.RegionRepository;
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
import java.util.List;
import java.util.Set;

/**
 * Created by chendeyu on 2015/12/24.
 */
@Controller
@RequestMapping("/site")
public class SiteController {
    private static final Log log = LogFactory.getLog(SiteController.class);

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

    /**
     * 站点列表页面
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/siteList")
//    @AuthorizeRole(roleType = AuthorizeRole.Role.Customer)
    public ModelAndView siteList(HttpServletRequest request) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/view/web/siteList.html");
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
    public ModelAndView addSite(HttpServletRequest request) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/view/web/addSite.html");
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
    public ResultView updateSite(Site site) {
        ResultView result = null;
        try {
            if(site!=null){
                Site site1=siteService.getSite(site.getSiteId());
                if(site1!=null){
                    site1.setCopyright(site.getCopyright());
                    site1.setDescription(site.getDescription());
                    site1.setKeywords(site.getKeywords());
                    site1.setLogoUri(site.getLogoUri());
                    site1.setName(site.getName());
                    site1.setTitle(site.getTitle());
                    site1.setUpdateTime(LocalDateTime.now());
                    siteService.save(site1);
                    result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
                }else{
                    result = new ResultView(ResultOptionEnum.NOFIND.getCode(), ResultOptionEnum.NOFIND.getValue(), null);
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            result = new ResultView(ResultOptionEnum.FAILE.getCode(), ResultOptionEnum.FAILE.getValue(), null);
        }
        return result;
    }

    /**
     * 修改站点页面
     * @param id
     * @param customerId
     * @return
     * @throws Exception
     */
    @RequestMapping("/updateSite")
    public ModelAndView updateSite(@RequestParam(value = "id", defaultValue = "0") Long id, int customerId) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.setViewName("/view/web/updateSite.html");
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
                    modelAndView.addObject("homeDomain",hostService.getHomeDomain(site));
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
     * @param id
     * @param customerId
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteSite", method = RequestMethod.POST)
    @ResponseBody
    public ResultView deleteModel(@RequestParam(name = "id", required = true, defaultValue = "0") Long id, int customerId, HttpServletRequest request) {
        ResultView result = null;
        try {
            if (cookieUser.isSupper(request)) {
                Site site = siteService.findBySiteIdAndCustomerId(id, customerId);
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
}