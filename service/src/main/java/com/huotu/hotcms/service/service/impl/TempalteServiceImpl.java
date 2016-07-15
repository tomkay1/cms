/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.repository.TemplateRepository;
import com.huotu.hotcms.service.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by wenqi on 2016/7/15.
 */
@Service
public class TempalteServiceImpl implements TemplateService {

    @Autowired
    private TemplateRepository templateRepository;

    //使用Redis
    @Override
    @Transactional
    public boolean laud(long siteId, long customerId) {
        return false;
    }
}
