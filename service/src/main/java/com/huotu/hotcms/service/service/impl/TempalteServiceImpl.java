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
import com.huotu.hotcms.service.repository.*;
import com.huotu.hotcms.service.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wenqi on 2016/7/15.
 */
@Service
public class TempalteServiceImpl implements TemplateService {

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PageInfoRepository pageInfoRepository;
    @Autowired
    private AbstractContentRepository abstractContentRepository;
    @Autowired
    private SiteRepository siteRepository;

    //使用Redis
    @Override
    @Transactional
    public boolean laud(long siteId, long customerId) {
        return false;
    }

    @Override
    public void use(long templateSiteID, long customerSiteId, int mode) {
        Site templateSite=siteRepository.findOne(templateSiteID);
        Site customerSite=siteRepository.findOne(customerSiteId);
        if(1==mode){
            delete(customerSite);
        }
        copy(templateSite,customerSite);
    }

    /**
     * 删掉原先站点下的数据
     * @param customerSite
     */
    private void delete(Site customerSite) {

    }

    /**
     * 复制
     * @param templateSite 模板站点
     * @param customerSite 商户站点
     */
    private void copy(Site templateSite,Site customerSite){
        List<Category> categories=categoryRepository.findBySite(templateSite);
    }
}
