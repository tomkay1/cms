/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget;

import org.springframework.core.io.Resource;

/**
 * 页面主题
 *
 * @author CJ
 */
public interface PageTheme {

    /**
     * 主色调
     * @return 可以为null
     */
    String mainColor();

    // todo more styles



    /**
     * 高级用户自定义的LESS
     *
     * @return LESS资源
     */
    Resource customLess();

}
