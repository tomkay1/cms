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
});
