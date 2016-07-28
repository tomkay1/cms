/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.SystemString;
import com.huotu.hotcms.service.repository.SystemStringRepository;
import me.jiangcai.lib.upgrade.VersionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author CJ
 */
@Service
public class VersionInfoServiceImpl implements VersionInfoService {

    @Autowired
    private SystemStringRepository systemStringRepository;

    @Override
    public <T extends Enum> T currentVersion(Class<T> type) {
        SystemString systemString = systemStringRepository.findOne(type.getName());
        if (systemString == null)
            return null;
        for (T t : type.getEnumConstants()) {
            if (String.valueOf(t.ordinal()).equals(systemString.getValue()))
                return t;
        }
        return null;
    }

    @Override
    public <T extends Enum> void updateVersion(T currentVersion) {
        SystemString systemString = systemStringRepository.findOne(currentVersion.getClass().getName());
        if (systemString == null) {
            systemString = new SystemString();
            systemString.setId(currentVersion.getClass().getName());
        }
        systemString.setValue(String.valueOf(currentVersion.ordinal()));
        systemStringRepository.save(systemString);
    }
}
