/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

define(function(require, exports, module) {
    require('../../libs/jquery.htmlClean.js');

    var DataHandle  = {
        createROOT: function (elements, url) {
            var json = {};
            var data = [];
            var root = $(elements).children();
            $.each(root, function (i, v) {
                var child = {};
                child.layout = $(v).data('layout');
                child.components = DataHandle.traversalDOM2Json(v);
                data.push(child);
            });
            json.elements = data;
            DataHandle.ajaxData(json, url);
        },
        traversalDOM2Json: function (elements) {
            var result = []
            var children = $(elements).children().children();
            $.each(children, function (i, v) {
                result.push(DataHandle.distinguishDOMType(v));
            });
            return result;
        },
        distinguishDOMType: function (elements) {
            var childJSON = {};
            if ( $(elements).hasClass('row') ) {
                childJSON.layout = $(elements).data('layout');
                childJSON.components = DataHandle.traversalDOM2Json(elements);
            } else {
                childJSON.widgetIdentity = $(elements).data('widgetIdentity');
                childJSON.style = 0;
                childJSON.properties = {};
            }
            return childJSON;
        },
        changeStructure: function(e, t) {
            $("#pageHTML ." + e).removeClass(e).addClass(t)
        },
        cleanHtml: function(e) {
            $(e).parent().append($(e).children().html());
        },
        downloadLayoutSrc: function (url) {
            var save = $('<div></div>');
            save.html($("#pageHTML").html());
            var t = save;
            t.find(".setting, .drag, .remove, .preview").remove();
            t.find(".ncrow").addClass("removeClean");
            t.find(".box-element").addClass("removeClean");
            t.find(".ncrow .ncrow .ncrow .ncrow .ncrow .removeClean").each(function () {
                DataHandle.cleanHtml(this)
            });
            t.find(".ncrow .ncrow .ncrow .ncrow .removeClean").each(function () {
                DataHandle.cleanHtml(this)
            });
            t.find(".ncrow .ncrow .ncrow .removeClean").each(function () {
                DataHandle.cleanHtml(this)
            });
            t.find(".ncrow .ncrow .removeClean").each(function () {
                DataHandle.cleanHtml(this)
            });
            t.find(".ncrow .removeClean").each(function () {
                DataHandle.cleanHtml(this)
            });
            t.find(".removeClean").each(function () {
                DataHandle.cleanHtml(this)
            });
            t.find(".removeClean").remove();
            t.find(".column").removeClass("ui-sortable");
            t.find(".row-fluid").removeClass("clearfix").children().removeClass("column");
            if (t.find(".container").length > 0) {
                DataHandle.changeStructure("row-fluid", "row")
            }
            formatSrc = $.htmlClean(save.html(), {
                format: true,
                allowedAttributes: [
                    ["id"],
                    ["class"],
                    ["data-toggle"],
                    ["data-target"],
                    ["data-parent"],
                    ["role"],
                    ["data-dismiss"],
                    ["aria-labelledby"],
                    ["aria-hidden"],
                    ["data-slide-to"],
                    ["data-slide"],
                    ["data-layout"]
                ]
            });
            var root = $('<div></div>');
            root.html(formatSrc);
            DataHandle.createROOT($(root), url);
        },
        //用于保存 数据时候，发起请求
        ajaxData: function (data, url) {
            // $.ajax({
            //     type: 'POST',
            //     url: url,
            //     data: data ,
            //     success: function (msg) {
            //         console.log(msg);
            //     }
            // });
        },
        init: function (url) {
            DataHandle.downloadLayoutSrc(url);
        }
    };
    exports.init = function () {
        var url = 'index.php';//save url
        $('#saveBtn').on('click', function () {
            DataHandle.init(url);
        });
    }
});