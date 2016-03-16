/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.common;

/**
 * Created by xhl on 2015/12/23.
 */
public class ConfigInfo {
//    @Value("${web.rootTemplate}")
    private static final String rootTemplate="/template/%s";

    /**
     * 获得本地模版根目录
     * */
    public static String getRootTemplate(Integer customerId){
        return String.format(rootTemplate,customerId);
    }

}
