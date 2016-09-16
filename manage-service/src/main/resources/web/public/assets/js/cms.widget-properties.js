/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

/**
 * 提供尽量简单的方式,让用户更改widget-properties
 * Created by CJ on 9/16/16.
 */

/**
 *
 * <span class="gallery-content" data-id="myChoose"></span>
 * <span class="aritcle-content" id="myChoose"></span>
 * <span class="aritcle-category" id="myChoose"></span>
 */

CMSWidgets.plugins.properties = {};

CMSWidgets.plugins.properties.open = function (globalId, identity, editAreaElement) {

    console.log('hello world', editAreaElement);

    $('.gallery-content', editAreaElement).each(function (index, data) {
        var properties = widgetProperties(globalId);
        var value = properties[data.attr('id')];
        if (value) {
            data.html('<h1>hajahah</h1>');
        } else {
            data.html('<button>change me</button>')
        }
    });

};