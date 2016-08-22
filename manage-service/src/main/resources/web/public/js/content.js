/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

$(function () {
    $.cmsUploader($('#article-uploader, #link-uploader, #video-uploader, #gallery-uploader'), function (path) {
        $("#thumbUri").val(path);
    }, {
        allowedExtensions: ['jpeg', 'jpg', 'png', 'bmp'],
        itemLimit: 1,
        sizeLimit: 3 * 1024 * 1024
    });

    $.cmsUploader($('#download-uploader'), function (path) {
        $("#downloadUrl").val(path);
    });

    var categories = $('option', $('#categories'));

    var parentCategoryId = $('select[name=parentCategoryId]');

    var categoryName = $('input[name=categoryName]');
    categoryName.change(function () {

        // 找到那个option
        var val = $(this).val();
        // 我们找下是否在列表内

        var targetOption = categories.filter(function (index, ele) {
            return $(ele).val() == val;
        }).first();

        if (targetOption.size() > 0) {
            // 的确是选择的
            parentCategoryId.prop('disabled', 'disabled');
            var parentId = targetOption.attr('parentId');
            if (parentId && parentId.length > 0) {
                //有父类的
                parentCategoryId.val(parentId);
            } else {
                //置空
                parentCategoryId.val('');
            }
        } else {
            // 手工录入的 保留原值么?
            parentCategoryId.prop('disabled', '');
        }

        console.log('changed', val, categories, targetOption);

    });

});

