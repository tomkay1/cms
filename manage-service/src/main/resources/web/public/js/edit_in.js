/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

/**
 * 内内容管理页所用的脚本
 * Created by CJ on 9/19/16.
 */

$(function () {
    $.ajax(dataURI + "?siteId=" + siteId, {
        async: !$.testMode,
        dataType: 'json',
        success: function (data) {

            var listGroup = $('.list-group');
            // active on chose
            listGroup.empty();
            $.each(data, function (index, row) {
                listGroup.append('<a name="' + row.serial + '" href="' + row.uri + '" class="list-group-item" target="content">' +
                    '<span class="badge">' + row.badge + '</span>' + row.title + '</a>');
                var links = $('a', listGroup);
                links.get(links.size() - 1).originRow = row;
            });

            var items = $('.list-group-item', listGroup);

            function doSelect(ele) {
                console.error('select me');
                var row = $(ele).get(0).originRow;
                if (!row)
                    return;
                parent.$(parent.document).trigger('content-changed', row);
            }

            items.click(function () {
                items.removeClass('active');
                $(this).addClass('active');
                if ($.unitTestMode) {
                    doSelect(this);
                }
            });

            items.dblclick(function () {
                doSelect(this);
            });
        }
    });

    $('#sidebarContent').niceScroll({
        'cursoropacitymax': 0.6
    });
});