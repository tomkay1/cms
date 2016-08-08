/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

/**
 * 数据保存遍历生成 JSON 数据传给服务器
 * createROOT json.elements 改为 json.root 标示为顶级数据
 * 改写 traversalDOM2Json 使 elements 为多维数组
 *
 * init 初始化 url 如果为null表示处于原型测试
 */
var DataHandle = {
    createROOT: function (elements, url) {
        var json = {};
        var data = [];
        var root = $(elements).children();
        $.each(root, function (i, v) {
            var child = {};
            child.layout = {};
            child.layout.value = $(v).attr('data-layout-value');
            child.layout.elementGroups = DataHandle.traversalDOM2Json(v);
            data.push(child);
        });
        json.root = data;
        DataHandle.ajaxData(json, url);
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
            childJSON.layout = {};
            childJSON.layout.value = $(elements).attr('data-layout-value');
            childJSON.layout.elementGroups = DataHandle.traversalDOM2Json(elements);
        } else {
            childJSON.component = {};
            childJSON.component.widgetIdentity = $(elements).attr('data-widgetidentity');
            childJSON.component.id = $(elements).attr('id');
            childJSON.component.styleId = $(elements).attr('data-styleid');
            childJSON.component.properties = wsCache.get($(elements).attr('id')) ? wsCache.get($(elements).attr('id')).properties : {};
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
                ["data-styleid"]
            ]
        });
        var root = $('<div></div>');
        console.log(formatSrc);
        root.html(formatSrc);
        DataHandle.createROOT($(root), url);
    },
    //用于保存 数据时候，发起请求
    ajaxData: function (data, url) {
        data = JSON.stringify(data);
        if (savePage == null) {
            layer.alert(data);
        }
        console.log(data);
        $.ajax({
            type: 'PUT',
            url: url,
            data: data,
            dataType: 'json',
            success: function (msg) {
                console.log(msg);
                //Todo 保存成功后需要跳转的页面
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log(errorThrown);
            }
        });
    },
    init: function (url) {
        if (!url) DataHandle.downloadLayoutSrc(url);
    }
};

/**
 * 页面 Layout 和 Component 模板代码
 * @type {{layout: string[], component: string[]}}
 */
var DOM = {
    layout: [
        '<div class="ncrow ui-draggable" style="display: block;">',
        '<span class="remove label label-danger"><i class="icon-cancel"></i>删除</span>',
        '<span class="drag label label-default"><i class="icon-move"></i>拖动</span>',
        '<div class="view">',
        '<div class="row clearfix"></div>',
        '</div>',
        '</div>'
    ],
    component: [
        '<div class="box box-element ui-draggable" style="display: block;">',
        '<span class="setting label label-primary" ><i class="icon-cog"></i> 设置</span>',
        '<span class="drag label label-default"><i class="icon-move"></i> 拖动</span>',
        '<span class="remove label label-danger"><i class="icon-cancel"></i> 删除</span>',
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
            success: function (pageJson) {
                if (!$.isEmptyObject(pageJson)) {
                    CreatePage.createTopLayout(pageJson);
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log(errorThrown);
            }
        });
    },
    createTopLayout: function (data) {
        var pageData = data.root;
        var container = $('#pageHTML');
        $.each(pageData, function (i, v) {
            container.append(DOM.layout.join('\n'));
            var ele = container.children('.ncrow').children('.view').children('.row');
            ele.eq(i).attr('data-layout-value', v.layout.value);
            var column = CreatePage.createColumn(v.layout);
            ele.eq(i).html(column);
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
        container.children('.box').children('view').html('data-target', data.previewHTML);
        return container.html();
    },
    createLayout: function (data) {
        var container = $('<div></div>');
        container.append(DOM.layout.join('\n'));
        var ele = container.children('.ncrow').children('.view').children('.row');
        ele.attr('data-layout-value', data.value);
        var column = CreatePage.createColumn(data);
        ele.html(column);
        return container.html();
    },
    init: function (url) {
        if (!url) url = '../../public/assets/js/data/reload.json';
        CreatePage.getPage(url);
    }
};

var dataHandle = {};
dataHandle.init = function () {
    if (!savePage)
        CreatePage.init(null);
    else
        CreatePage.init(savePage + pageId);


    $('#saveBtn').on('click', function () {
        if (!savePage)
            DataHandle.init(null);
        else
            DataHandle.init(savePage + pageId);
    })
};
dataHandle.init();

