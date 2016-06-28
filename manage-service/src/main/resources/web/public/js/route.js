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

    $.validator.addMethod('routeRule', function (value, element) {
        try {
            new RegExp(value);
            return true;
        } catch (e) {
            return false;
        }
    }, '规则必须是一个正则表达式');

    var routeForm = $('#routeForm');
    routeForm.validate({
        rules: {
            rule: {
                required: true,
                routeRule: true
            },
            targetUri: {
                required: true
            },
            description: {
                maxlength: 200
            }
        },
        messages: {
            rule: {
                required: "规则为必输项",
                routeRule: '规则必须是一个有效的正则表达式'
            },
            targetUri: {
                required: "目标地址为必输项"
            }
        }
    });
});