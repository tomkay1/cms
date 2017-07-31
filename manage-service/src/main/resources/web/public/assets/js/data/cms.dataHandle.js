/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

/**
 * 数据保存遍历生成 JSON 数据传给服务器
 * createROOT json.elements 改为 json.root 标示为顶级数据
 * 改写 traversalDOM2Json 使 elements 为多维数组
 *
 * init 初始化 url 如果为null表示处于原型测试
 */
var DataHandle = {
    createROOT: function (elements) {
        var json = {};
        var data = [];
        var root = $(elements).children();
        $.each(root, function (i, v) {
            var child = {};
            var styleSheet = $(v).attr('data-stylesheet');

            child.layout = {};
            child.layout.value = $(v).attr('data-layout-value');
            child.layout.styleSheet = styleSheet? JSON.parse(styleSheet) : styleSheet;
            child.layout.elementGroups = DataHandle.traversalDOM2Json(v);

            data.push(child);

        });
        json.root = data;
        json.styleSheet = LayoutSetting.rootStylesheet;
        return json;
    },
    traversalDOM2Json: function (elements) {
        var result = [];
        var ele = $(elements).children();
        $.each(ele, function (i, v) {
            var html = $.trim($(v).html());
            if (html == '') {
                var arr = [];
                var key = {};
                key.empty = {};
                arr.push(key);
                result.push(arr);
            } else {
                var children = $(this).children();
                var r = [];
                $.each(children, function (i, v) {
                    r.push(DataHandle.distinguishDOMType(v));
                });
                result.push(r);
            }
        });
        return result;
    },
    distinguishDOMType: function (elements) {
        var childJSON = {};
        if ($(elements).hasClass('row')) {
            var styleSheet = $(elements).attr('data-stylesheet');

            childJSON.layout = {};
            childJSON.layout.value = $(elements).attr('data-layout-value');
            childJSON.layout.styleSheet = styleSheet ? JSON.parse(styleSheet) : styleSheet;
            childJSON.layout.elementGroups = DataHandle.traversalDOM2Json(elements);
        } else {
            if ( $(elements).attr('id').indexOf('errorPlaceholder') == -1) {
                childJSON.component = {};
                childJSON.component.widgetIdentity = $(elements).attr('data-widgetidentity');
                childJSON.component.id = $(elements).attr('id');
                childJSON.component.styleId = $(elements).attr('data-styleid') || null;
                childJSON.component.properties = wsCache.get($(elements).attr('id')) ? wsCache.get($(elements).attr('id')).properties : {};
            } else {
                childJSON.empty = {};
            }

        }
        return childJSON;
    },
    changeStructure: function (e, t) {
        $("#pageHTML ." + e).removeClass(e).addClass(t)
    },
    cleanHtml: function (e) {
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
                ["data-styleid"],
                ["data-stylesheet"]
            ]
        });
        var root = $('<div></div>');
        root.html(formatSrc);
        return DataHandle.createROOT($(root));
    },
    //用于保存 数据时候，发起请求
    ajaxData: function (data, url) {
        var DATA = JSON.stringify(data);
        if (savePage == null) {
            layer.alert(DATA);
        }
        $.ajax({
            type: 'PUT',
            url: url,
            contentType: "application/json; charset=utf-8",
            data: DATA,
            statusCode: {
                400: function () {
                    layer.msg('部分控件参数错误或者不足，页面保存失败。', {time: 2000});
                    editFunc.closePreloader();
                },
                202: function () {
                    layer.msg('保存成功！', {time: 2000});
                    //Todo 保存成功后需要跳转的页面
                },
                403: function() {
                    layer.msg('没有权限', {time: 2000});
                    editFunc.closePreloader();
                },
                404: function() {
                    layer.msg('服务器请求失败', {time: 2000});
                    editFunc.closePreloader();
                },
                502: function () {
                    layer.msg('服务器错误,请稍后再试', {time: 2000});
                    editFunc.closePreloader();
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log(errorThrown);
                layer.msg('服务器错误,请稍后再试', {time: 2000});
            }
        });
    },
    ajaxPreview: function (data, url) {
        var form = $('#previewForm');
        form.find('.js-json-string').val(JSON.stringify(data));
        form.submit();
    },
    save: function (url) {
        var data = DataHandle.downloadLayoutSrc();
        DataHandle.ajaxData(data, url);
    },
    preview: function (url) {
        var data = DataHandle.downloadLayoutSrc();
        DataHandle.ajaxPreview(data, url);
    }
};

/**
 * 页面 Layout 和 Component 模板代码
 * @type {{layout: string[], component: string[]}}
 */
var DOM = {
    layout: [
        '<div class="ncrow ui-draggable" style="display: block;">',
        '<span class="setting layout-setting label label-primary" data-target="layout-setting"><i class="fa fa-cog"></i> 布局设置</span>',
        '<span class="drag label label-default"><i class="fa fa-arrows"></i> 拖动</span>',
        '<span class="remove label label-danger"><i class="fa fa-times"></i> 删除</span>',
        '<div class="view">',
        '<div class="row clearfix"></div>',
        '</div>',
        '</div>'
    ],
    component: [
        '<div class="box box-element ui-draggable" style="display: block;">',
        '<span class="setting label label-primary" ><i class="fa fa-cog"></i> 设置</span>',
        '<span class="drag label label-default"><i class="fa fa-arrows"></i> 拖动</span>',
        '<span class="remove label label-danger"><i class="fa fa-times"></i> 删除</span>',
        '<div class="view"></div>',
        '</div>'
    ]
};

/**
 * 获取服务器页面JSON数据，重绘到工作区
 * getPage: 请求服务器，如有返回值，获取页面信息JSON
 * createTopLayout: 绘制顶层 Layout
 * createColumn: 绘制 Column
 * createColumnDom: 绘制 Column代码，并使用递归插入内容
 * createComponent: 绘制组件代码
 * createLayout: 绘制布局代码
 * init: 外部调用方法 url 如果为null表示处于原型测试
 */
var CreatePage = {
    getPage: function (url) {
        $.ajax({
            type: 'GET',
            url: url,
            dataType: 'json',
            statusCode: {
                403: function() {
                    layer.msg('没有权限', {time: 2000});
                    editFunc.closePreloader();
                },
                404: function() {
                    layer.msg('服务器请求失败', {time: 2000});
                    editFunc.closePreloader();
                },
                502: function () {
                    layer.msg('服务器错误,请稍后再试', {time: 2000});
                    editFunc.closePreloader();
                }
            },
            success: function (pageJson) {
                if (!$.isEmptyObject(pageJson)) {
                    CreatePage.createTopLayout(pageJson);
                    editFunc.dragFunc();
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log(errorThrown);
                layer.msg('服务器错误,请稍后再试', {time: 2000});
            }
        });
    },
    createTopLayout: function (data) {
        var pageData = data.root;
        // 存储获取的 CSS 参数
        CreatePage.setRootStyle(data.styleSheet);
        var container = $('#pageHTML');
        $.each(pageData, function (i, v) {
            container.append(DOM.layout.join('\n'));
            var ele = container.children('.ncrow').children('.view').children('.row');
            ele.eq(i).attr('data-layout-value', v.layout.value);
            ele.eq(i).attr('data-stylesheet', JSON.stringify(v.layout.styleSheet));
            var column = CreatePage.createColumn(v.layout);
            ele.eq(i).html(column);
            CreatePage.setStyle(ele.eq(i), v.layout.styleSheet);
        });
    },
    createColumn: function (data) {
        var container = $('<div></div>');
        var arr = data.value.toString().split(',');
        $.each(arr, function (i, v) {
            var column;
            if (v == 100) {
                column = $('<div class="column ui-sortable"></div>');
            } else if (v == 50) {
                column = $('<div class="container column ui-sortable"></div>');
            } else {
                column = $('<div class="column ui-sortable"></div>');
                var col = 'col-md-' + v;
                column.addClass(col);
            }
            column.html(CreatePage.createColumnDom(data.elementGroups[i]));
            container.append(column);
        });
        return container.html();
    },
    createColumnDom: function (data) {
        var container = $('<div></div>');
        $.each(data, function (i, v) {
            $.each(v, function (key, val) {
                if (key == 'component') {
                    container.append(CreatePage.createComponent(val));
                }
                if (key == 'layout') {
                    container.append(CreatePage.createLayout(val));
                }
                if (key == 'empty') {
                    console.log('It\'s Empty');
                }
            });
        });
        return container.html();
    },
    createComponent: function (data) {
        wsCache.set(data.id, {'properties': data.properties});
        var container = $('<div></div>');
        container.append(DOM.component.join('\n'));
        container.children('.box').children('span.setting').attr('data-target', data.widgetIdentity);
        container.children('.box').children('.view').html(data.previewHTML);
        container.children('.box').children('.view').children().attr({'id':data.id,'data-widgetidentity':data.widgetIdentity, 'data-styleid': data.styleId});
        return container.html();
    },
    createLayout: function (data) {
        var container = $('<div></div>');
        container.append(DOM.layout.join('\n'));
        var ele = container.children('.ncrow').children('.view').children('.row');
        ele.attr('data-layout-value', data.value);
        ele.attr('data-stylesheet', JSON.stringify(data.styleSheet));
        var column = CreatePage.createColumn(data);
        ele.html(column);
        CreatePage.setStyle(ele, data.styleSheet);
        return container.html();
    },
    setStyle: function (ele, data) {
        var e = ele.children('.column');

        $.each(data, function (k, v) {
            e.css(k,v);
        });
    },
    setRootStyle: function (data) {
        $('#pageHTML').attr('data-stylesheet', JSON.stringify(data));

        LayoutSetting.rootStylesheet = data;

        $.each(data, function (k, v) {
            $('#pageHTML').css(k,v);
        });
    },
    init: function (url) {
        if (!url) url = '../../public/assets/js/data/reload2.json';
        CreatePage.getPage(url);
    }
};

var dataHandle = {};
dataHandle.init = function () {
    if (!savePage)
        CreatePage.init(null);
    else
        CreatePage.init(savePage + pageId);


    $('#saveBtn').click(function () {
        if (!savePage)
            return false;
        else
            DataHandle.save(savePage + pageId);
    });

    $('#previewBtn').click(function () {
        if (!previewPage)
            return false;
        else
            DataHandle.preview(previewPage + pageId);
    });
};
dataHandle.init();
