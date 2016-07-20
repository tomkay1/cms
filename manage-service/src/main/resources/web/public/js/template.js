/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

/**
 * 关于站点的脚本
 * Created by CJ on 6/27/16.
 */
$(function () {

    var templateForm = $('#templateForm');

    templateForm.validate({
        rules: {
            name: {
                required: true
            }
        },
        messages: {
            name: {
                required: "名称为必输项"
            }
        }
    });

    $.cmsUploader($('#logo-uploader'), function (path) {
        $('input[name=extra]', templateForm).val(path);
    }, {
        allowedExtensions: ['jpeg', 'jpg', 'png', 'bmp'],
        itemLimit: 1,
        sizeLimit: 3 * 1024 * 1024
    });

});
