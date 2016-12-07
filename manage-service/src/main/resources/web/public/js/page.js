/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

/**
 * 关于页面的脚本
 * Created by CJ on 6/27/16.
 */
$(function () {
    var updatePageInfo = $('#updatePageInfo');
    var title = $('input[name=title]', updatePageInfo);
    var pagePath = $('input[name=pagePath]', updatePageInfo);

    $('.btnCommit', updatePageInfo).click(function () {
        if (!title.val())
            return;

        // 懒得响应
        function success() {
            top.showSuccess('成功', '修改成功');
            if (thisUrl) {
                window.location = thisUrl;
            }
        }

        function error() {
            top.showDanger('错误', '修改失败');
        }

        if (updatePageInfoUrl) {
            $.ajax({
                method: 'POST',
                data: {title: title.val(), pagePath: pagePath.val()},
                url: updatePageInfoUrl.replace('$1', updatePageInfo.id),
                success: success,
                error: error
            });
        } else {
            success();
        }
    });


    $('.fa-pencil-square-o').click(function () {
        updatePageInfo.id = $(this).closest('tr').attr('data-id');
        var marginTop = $(window.parent.document).scrollTop();
        updatePageInfo.css('margin-top', marginTop);
        $(this).parent().parent().siblings().each(function () {
            if ($(this).index() == 0) {
                title.val($(this).text());
            } else if ($(this).index() == 4) {
                pagePath.val($(this).text());
            }
        });
        updatePageInfo.modal();
    })
});
