/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.ImagesOwner;
import com.huotu.hotcms.service.ResourcesOwner;
import me.jiangcai.lib.resource.Resource;
import me.jiangcai.lib.resource.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 一些常规的服务
 *
 * @author CJ
 */
@Service
public class CommonService {

    @Autowired
    private ResourceService resourceService;

    /**
     * 删除它持有的所有资源
     *
     * @param resourcesOwner 资源拥有者,不可为null
     */
    public void deleteResource(ResourcesOwner resourcesOwner) throws IOException {
        for (String path : resourcesOwner.getResourcePaths()) {
            if (path != null)
                resourceService.deleteResource(path);
        }
    }


    /**
     * 从临时资源库更新图片资源,临时资源需要立刻回收
     *
     * @param owner 资源宿主
     * @param index 索引号
     * @param path  资源路径
     * @throws IllegalArgumentException 如果图片不是图片
     */
    public void updateImageFromTmp(ImagesOwner owner, int index, String path) throws IOException {
        if (!StringUtils.isEmpty(path)) {
            Resource tmp = resourceService.getResource(path);
            if (tmp.exists()) {
                try {
                    owner.updateImage(index, resourceService, path);
                } finally {
                    //noinspection ThrowFromFinallyBlock
                    resourceService.deleteResource(path);
                }
            }
        }
    }

    /**
     * 从临时资源库更新资源,临时资源需要立刻回收
     *
     * @param owner 资源宿主
     * @param index 索引号
     * @param path  资源路径
     */
    public void updateResourceFromTmp(ResourcesOwner owner, int index, String path) throws IOException {
        if (!StringUtils.isEmpty(path)) {
            owner.updateResource(index, resourceService, path);
            resourceService.deleteResource(path);
        }
    }
}
