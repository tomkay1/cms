/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

/**
 * Created by CJ on 6/25/16.
 */

// table
jQuery('#ownerTable').dataTable({
    language: {
        url: 'http://resali.huobanplus.com/cdn/bracket/localisation/dataTable_zh_CN.json'
    },
    sPaginationType: "full_numbers"
});

// 几个动作
$(function () {
    var customerIdChanger = $('#customerIdChanger');
    var customerIdInput = $('input', customerIdChanger);

    $('.btn-primary', customerIdChanger).click(function () {
        if (!customerIdInput.val())
            return;
        // 懒得响应
        function success() {

        }
        if (customerIdChangeUrl) {
            $.ajax({
                method: 'put',
                contentType: 'application/json',
                data: customerIdInput.val(),
                url: customerIdChangeUrl.replace('$1', customerIdChanger.id)
            });
        }
    });

    $('.glyphicon-shopping-cart').click(function () {
        customerIdChanger.id = $(this).closest('tr').attr('data-id');
        customerIdChanger.modal();
    });

    function changeOwnerEnable(className) {
        $('.' + className).click(function () {
            var me = $(this);
            var id = me.closest('tr').attr('data-id');

            var targetClass;
            var targetTitle;
            var currentClass;
            if (me.hasClass('glyphicon-play')) {
                currentClass = 'glyphicon-play';
                targetClass = 'glyphicon-pause';
                targetTitle = '暂停';
            } else {
                currentClass = 'glyphicon-pause';
                targetClass = 'glyphicon-play';
                targetTitle = '恢复';
            }
            function success() {
                console.log(currentClass, targetClass, targetTitle);
                me.removeClass(currentClass);
                me.addClass(targetClass);
                me.closest('a').attr('title', targetTitle);
            }

            function error() {
                alert('未知错误');
                console.error.apply(console, arguments);
            }

            if (toggleOwnerEnableUrl) {
                $.ajax({
                    method: 'put',
                    url: toggleOwnerEnableUrl.replace('$1', id),
                    success: success,
                    error: error
                });
            } else {
                success();
            }

        });
    }

    changeOwnerEnable('glyphicon-play');
    changeOwnerEnable('glyphicon-pause');

});