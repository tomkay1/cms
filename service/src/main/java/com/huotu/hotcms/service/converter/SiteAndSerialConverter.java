/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.converter;

import com.huotu.hotcms.service.model.SiteAndSerial;
import com.huotu.hotcms.service.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;

/**
 * @author CJ
 */
@Component
public class SiteAndSerialConverter extends AutowireConverter<SiteAndSerial> {

    @Autowired
    private SiteRepository siteRepository;

    @Override
    public SiteAndSerial convert(String source) {
        if (source == null)
            return null;
        String[] strings = source.split("@");

        if (strings.length != 2)
            throw new IllegalArgumentException("not supported convert " + source + " to SiteAndSerial.");

        return new SiteAndSerial(siteRepository.getOne(NumberUtils.parseNumber(strings[1], Long.class)), strings[0]);
    }
}
