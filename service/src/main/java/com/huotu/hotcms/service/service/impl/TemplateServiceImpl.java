/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.Template;
import com.huotu.hotcms.service.event.CopySiteEvent;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.service.repository.TemplateRepository;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.ContentService;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.service.service.TemplateService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class TemplateServiceImpl implements TemplateService {

    private static final Log log = LogFactory.getLog(TemplateServiceImpl.class);

    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ContentService contentService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SiteRepository siteRepository;

    private Map<String, Boolean> laudMap = new HashMap<>();

    //使用Redis
    @Override
    public boolean laud(long templateId, long ownerId, int behavior) {
        if (behavior == 1 && isLauded(templateId, ownerId))
            return false;
        Template template = templateRepository.findOne(templateId);
        try {
            String key = templateId + "$" + ownerId;
//            int laudNum = template.getLauds();
            if (1 == behavior) {//点赞
                template.setLauds(template.getLauds() + 1);
                laudMap.put(key, true);
            } else {
                template.setLauds(template.getLauds() - 1);
                laudMap.remove(key);
            }
            templateRepository.save(template);
            return true;
        } catch (Exception e) {
            log.warn("Unexpected", e);
            return false;
        }
    }

    @Override
    public void preview(Template template) {
        template.setScans(template.getScans() + 1);
    }

    @Override
    public void use(long templateSiteID, long customerSiteId, int mode) throws IOException {
        Template template = templateRepository.findOne(templateSiteID);
        Site site = siteRepository.findOne(customerSiteId);
        if (1 == mode) {
            siteService.deleteData(site);
        }
        copy(template, site);
        template.setUseNumber(template.getUseNumber() + 1);//使用数+1
//        template.setEnabled(true);
        templateRepository.save(template);
    }

    @Override
    public boolean isLauded(long templateId, long ownerId) {
        //目前只是简单实现
        String key = templateId + "$" + ownerId;
        return laudMap.get(key) != null;
    }

    /**
     * 复制
     *
     * @param from 源站点
     * @param to   商户站点
     */
    private void copy(Site from, Site to) throws IOException {
        // 考虑到 有一些数据源具有上下级，所以应该先把没有上级的 弄好，再弄带这些上级的 以此类推。
        Set<Category> categories = null;

        while (true) {
            if (categories == null)
                categories = categoryRepository.findBySiteAndParentNull(from);
            else
                categories = categoryRepository.findBySiteAndParentIn(from, categories);
            if (categories.isEmpty())
                break;
            // 执行copy
            for (Category category : categories) {
                Category newOne = categoryService.copyTo(category, to);
                //Page信息的复制
//                copyPageInfo(category, copyCategory, to);
                //对内容的复制
                contentService.copyTo(category, newOne);
            }
        }

        applicationEventPublisher.publishEvent(new CopySiteEvent(from, to));
    }

}
