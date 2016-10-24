/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

/**
 * 关于数据源的脚本
 * Created by Neo on 2016/9/13.
 */
$(function () {
    $('.js-add-category').click(function () {
        $('.minimize', '#categoryForm').each(function () {
            if ($(this).hasClass('maximize')) {
                $(this).trigger('click');
            }
        });
    });

    var contentTypeSelect = $('select[name=contentType]');

    var changed = function () {
        var contentTypeVal = contentTypeSelect.val();
        // console.log(contentTypeSelect, contentTypeVal);
        // 8 9
        // 其他的话 都清空掉 如果是8 9 则展示之
        var group = contentTypeSelect.closest('.form-group');
        var panel = group.closest('div[class!=form-group]');
        // 检查下目前的情况
        // 我们把额外标记都放在第一个group里面
        if (contentTypeVal == '8') {
            $('.mallClassCategory', panel).hide();
            $('.mallProductCategory', panel).show();
        } else if (contentTypeVal == '9') {
            $('.mallClassCategory', panel).show();
            $('.mallProductCategory', panel).hide();
        } else {
            $('.mallClassCategory', panel).hide();
            $('.mallProductCategory', panel).hide();
        }
    };

    contentTypeSelect.change(changed);
    changed();
});
