/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.widget.service.impl.huobanplus;

import com.huotu.hotcms.service.widget.service.impl.AbstractGoodsCategoryService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;


/**
 * 商品分类组件服务
 * Created by cwb on 2016/3/21.
 */
@Profile("container")
@Service
public class GoodsCategoryServiceImpl extends AbstractGoodsCategoryService {

    private static final Log log = LogFactory.getLog(GoodsCategoryServiceImpl.class);

    @Autowired
    public void setEnv(Environment env) {
        this.host = env.getProperty("com.huotu.huobanplus.open.api.root");
        if(host==null) {
            throw new IllegalStateException("请设置com.huotu.huobanplus.open.api.root属性");
        }
    }
}
