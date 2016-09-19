/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller.support;

import com.huotu.cms.manage.exception.RedirectException;
import com.huotu.cms.manage.exception.SiteRequiredException;
import com.huotu.hotcms.service.SiteResource;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.service.SiteService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 管理站点内容的控制器,需要当前登录操作人员已经选择好站点
 *
 * @author CJ
 */
public abstract class SiteManageController<T, ID extends Serializable, PD, MD> extends CRUDController<T, ID, PD, MD> {

    private static final Log log = LogFactory.getLog(SiteManageController.class);

    @Autowired
    private SiteService siteService;
    @Autowired
    private ServletContext servletContext;

    /**
     * @param locale
     * @return 正在管理的这项资源的名字
     */
    protected abstract String resourceName(Locale locale);

    @Override
    protected Specification<T> prepareIndex(Login login, Model model, RedirectAttributes attributes) throws RedirectException {
        Site site = checkSite(login);
        return prepareIndex(login, site, model, attributes);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE, value = "/editIn")
    public String editIn(@AuthenticationPrincipal Login login, Locale locale, Long siteId, Model model) {
        Site site = checkSite(login, siteId);

        model.addAttribute("title", "选择" + resourceName(locale));
        model.addAttribute("name", resourceName(locale));
        model.addAttribute("insertUri", rootUri());
        model.addAttribute("insertable", true);
        model.addAttribute("site", site);

        return "view/edit_in.html";
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    @ResponseBody
    public Object json(@AuthenticationPrincipal Login login, Long siteId, Model model, RedirectAttributes attributes)
            throws RedirectException {
        try {
            Site site = checkSite(login, siteId);

            Specification<T> specification = prepareIndex(login, site, model, attributes);

            List<T> list;
            if (specification == null)
                list = jpaRepository.findAll();
            else
                list = jpaSpecificationExecutor.findAll(specification);

            return list.stream()
                    .filter(x -> x instanceof SiteResource)
                    .map(x -> {
                        SiteResource resource = (SiteResource) x;
                        Map<String, Object> data = new HashMap<>();
                        data.put("title", resource.getTitle());
                        data.put("uri", servletContext.getContextPath() + rootUri() + "/" + resource.getId()
                                + "?quick=true");
                        data.put("badge", "");
                        data.put("serial", resource.getSerial());
                        return data;
                    })
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException ex) {
            throw new RedirectException(rootUri(), ex);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            log.info("unknown exception on index", ex);
            // 这里可不能返回root 不是就死循环了,  应该返回
            throw new RedirectException("/manage", ex);
        }
    }

    @NotNull
    private Site checkSite(@AuthenticationPrincipal Login login, Long siteId) {
        Site site;
        if (siteId != null) {
            site = siteService.getSite(siteId);
            if (site == null || !login.siteManageable(site)) {
                throw new RedirectException("/manage/site", "你无权操作。");
            }
        } else
            site = checkSite(login);
        return site;
    }

    @NotNull
    private Site checkSite(Login login) throws RedirectException {
        Long siteId = login.currentSiteId();
        if (siteId == null) {
            throw new SiteRequiredException();
//            throw new RedirectException("/manage/site", "请先选择一个站点,再进行操作");
        }
        Site site = siteService.getSite(siteId);
        if (site == null || !login.siteManageable(site)) {
            throw new RedirectException("/manage/site", "你无权操作。");
        }
        return site;
    }

    @Override
    protected T preparePersist(HttpServletRequest request, Login login, T data, PD extra, RedirectAttributes attributes)
            throws RedirectException {
        Site site = checkSite(login);

        return preparePersist(login, site, data, extra, attributes);
    }


    /**
     * 默认会认为资源会有一个名为<code>site</code>的字段并以此作为过滤条件
     *
     * @param login
     * @param site       当前站点
     * @param model
     * @param attributes
     * @return
     * @see CRUDController#prepareIndex(Login, Model, RedirectAttributes)
     */
    @SuppressWarnings({"WeakerAccess", "JavaDoc"})
    protected Specification<T> prepareIndex(Login login, Site site, Model model, RedirectAttributes attributes)
            throws RedirectException {
        return (root, query, cb) -> cb.equal(root.get("site").as(Site.class), site);
    }

    /**
     * @param login
     * @param site       当前站点
     * @param data
     * @param extra
     * @param attributes
     * @return
     * @see CRUDController#preparePersist(HttpServletRequest, Login, Object, Object, RedirectAttributes)
     */
    @SuppressWarnings({"WeakerAccess", "JavaDoc"})
    protected abstract T preparePersist(Login login, Site site, T data, PD extra, RedirectAttributes attributes)
            throws RedirectException;

}
