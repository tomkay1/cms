/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

/**
 * 路由相关
 * Created by CJ on 6/28/16.
 */

$(function () {

    var widgetForm = $('#widgetForm');
    widgetForm.validate({
        rules: {
            groupId: {
                required: true
            },
            widgetId: {
                required: true
            },
            version: {
                required: true
            },
            type: {
                required: true
            }
        },
        messages: {
            groupId: {
                required: '需要输入groupId'
            },
            widgetId: {
                required: '需要输入widgetId'
            },
            version: {
                required: '需要输入version'
            },
            type: {
                required: '需要明确一个类型'
            }
        }
    });
});