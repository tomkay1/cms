/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

    var DataHandle  = {
        createROOT: function (elements, url) {
            var json = {};
            var data = [];
            var root = $(elements).children();
            $.each(root, function (i, v) {
                var child = {};
                child.layout = {};
                child.layout.value = $(v).attr('data-layout-value');
                child.layout.elements = DataHandle.traversalDOM2Json(v);
                data.push(child);
            });
            json.pageIdentity = '1';
            json.title = 'test';
            json.elements = data;
            DataHandle.ajaxData(json, url);
        },
        traversalDOM2Json: function (elements) {
            var result = [];
            var key = {};
            var ele = $(elements).children();
            $.each(ele, function (i, v) {
                var html = $.trim($(v).html());
                if ( html == '') {
                    key.empty = {};
                    result.push(key);
                } else {
                    var children = $(this).children();
                    $.each(children, function (i, v) {
                        result.push(DataHandle.distinguishDOMType(v));
                    });
                }
            });
            return result;
        },
        distinguishDOMType: function (elements) {
            var childJSON = {};
            if ( $(elements).hasClass('row') ) {
                childJSON.layout = {};
                childJSON.layout.value = $(elements).attr('data-layout-value');
                childJSON.layout.elements = DataHandle.traversalDOM2Json(elements);
            } else {
                childJSON.component = {};
                childJSON.component.widgetIdentity = $(elements).attr('data-widgetidentity');
                childJSON.component.id = $(elements).attr('id');
                childJSON.component.styleId = $(elements).attr('data-styleid');
                childJSON.component.properties = {};
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
                    ["data-layout"],
                    ["data-layout-value"],
                    ["data-widgetidentity"],
                    ["data-styleid"]
                ]
            });
            var root = $('<div></div>');
            root.html(formatSrc);
            DataHandle.createROOT($(root), url);
        },
        //用于保存 数据时候，发起请求
        ajaxData: function (data, url) {
            data=JSON.stringify(data);
            if(savePage==null){
                layer.alert(data);
            }
            console.log(data);
             $.ajax({
                 type: 'PUT',
                 url: url,
                 data: data ,
                 success: function (msg) {
                     console.log(msg);
                 }
             });
        },
        init: function (url) {
            DataHandle.downloadLayoutSrc(url);
        }
    };
    var dataHandle = {};
    dataHandle.init = function () {
        var strHref = window.document.location.href;
        var pageId=strHref.substring(strHref.lastIndexOf("/")+1);
        console.log(pageId)
        var url = savePage+pageId;//save url
        $('#saveBtn').on('click', function () {
            DataHandle.init(url);
        });
    };
dataHandle.init();

function widgetProperties(params){
    return {};
}
