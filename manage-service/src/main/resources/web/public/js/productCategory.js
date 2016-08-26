/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

/**
 * 产品数据源相关
 * Created by CJ on 8/26/16.
 */

$(function () {

    var fieldsContainer = $('#fieldsContainer');

    $('.fa-plus-circle').click(function () {
        // console.log('add new field');
        // 添加一个字段
        var fieldTemplate = $('#fieldTemplate');
        var ele = fieldTemplate.clone(true);

        ele.removeAttr('id');
        ele.appendTo(fieldsContainer);
        ele.show();
    });

    $('.fa-trash-o').click(function () {
        var field = $(this).closest('.field-form');
        field.remove();
    });

    $('.fa-caret-square-o-up').click(function () {
        var field = $(this).closest('.field-form');
        var pre = field.prev().filter('.field-form').filter(':visible');
        if (pre.size() > 0) {
            // 执行上移
            pre.before(field);
        }
    });

    $('.fa-caret-square-o-down').click(function () {
        var field = $(this).closest('.field-form');
        var next = field.next().filter('.field-form').filter(':visible');
        if (next.size() > 0) {
            // 执行下移
            next.after(field);
        }
    });


});

