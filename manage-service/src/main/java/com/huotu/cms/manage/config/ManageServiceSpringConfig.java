/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.config;

import me.jiangcai.lib.embedweb.EmbedWeb;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 载入manage-service的Spring配置类
 *
 * @author CJ
 */
@Configuration
@ComponentScan("com.huotu.cms.manage")
public class ManageServiceSpringConfig implements EmbedWeb {


    @Override
    public String name() {
        return "manage-service";
    }
}
