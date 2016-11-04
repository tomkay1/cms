/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

/**
 * https://github.com/huotuinc/CMS/issues/40
 * 依赖layer,editInURI,contentURI,galleryItemsURI
 *
 * Created by CJ on 03/11/2016.
 */
CRF = {};

/**
 * 我可以处理的class
 * @type {string}
 */
CRF.cssSelector = "[class*='article-content'],[class*='gallery-content']," +
    "[class*='link-content'],[class*='notice-content']," +
    "[class*='article-category'],[class*='gallery-category']," +
    "[class*='mallProduct-category'],[class*='mallClass-category']," +
    "[class*='link-category'],[class*='notice-category']";

/**
 * 工具函数,从给定的字符串中按照正则表达式截取指定的字符串
 * @param str
 */
CRF.interception = function (str) {
    var pattern = /((article|gallery|link|notice|mallClass|mallProduct)-(content|category))/g;
    var matching = pattern.exec(str);
    return matching[0];
};

CRF.exec = function (code, context) {

    if (typeof(code) == 'function') {
        return code.apply(context, Array.prototype.slice.call(arguments, 2));
    }
    // string?
    var currentWindow = window;

    do {
        var func = currentWindow[code];
        if (func) {
            // 父级找一下
            // 所有父级?
            return func.apply(context, Array.prototype.slice.call(arguments, 2));
        } else if (!currentWindow.parent || currentWindow.parent == currentWindow) {
            return eval(code);
        }
    } while (true);
};


/**
 * 根据资源类型和序列号以及站点ID生成指定url将返回的html渲染到指定元素中去
 * @param resourceName  资源类型
 * @param serial        序列号
 * @param ele           指定元素
 */
CRF.contentHTML = function (resourceName, serial, ele) {
    //var url="/manage/"+resourceName+"/render";
    var url = CRF.contentURI(resourceName, serial);
    $.ajax({
        type: "GET",
        url: url,
        dataType: "html",
        // data: {siteId: CMSWidgets.siteId, serial: serial},// 要提交的表单
        success: function (result) {
            $(ele).html(result);
            // return result;
        },
        error: function (e) {
            layer.msg("获取内容失败");
        }
    });


};

/**
 * 根据序列号生成元素里面的html代码
 * @param ele           元素
 * @param resourceName  资源名称
 * @param serial        序列号,未知的话尝试自己获取
 */
CRF.buildHtml = function (ele, resourceName, serial) {
    if (ele._store.render) {
        // 执行render
        $(ele).html(CRF.exec(ele._store.render, ele));
        return;
    }
    if (!serial) {
        // 执行 serial
        serial = CRF.exec(ele._store.serial, ele);
    }

    if (serial != undefined) {
        //ajax请求：返回html代码展现在制定元素中去
        CRF.contentHTML(resourceName, serial, ele);
    } else {
        var contentHTML = '<div>暂无数据</div>';
        $(ele).html(contentHTML);
    }

    if (!ele._store.name)
        return;
    var galleryItemAreaMatch = "[class*='gallery-item-area'][data-name='" + ele._store.name + "']";
    var galleryItemArea = $(galleryItemAreaMatch, ele._store.root);
    //先清理老数据
    galleryItemArea.filter('.gallery-item-area-clone').remove();
    //重新获取区域集合
    galleryItemArea = $(galleryItemAreaMatch, ele._store.root);
    if (galleryItemArea.size() > 0) {
        galleryItemArea.each(function (index, area) {
            // 此处area 是最原始的区域
            $(area).hide();
            // 寻找所有这个内容的item
            // 哦 前提是内容已设置
            if (serial) {
                $.ajax({
                    url: CRF.galleryItemsURI(serial),
                    dataType: 'json',
                    success: function (data) {
                        $.each(data, function (index, item) {
                            //每一个item 应该是展示一个区域
                            // deep clone
                            var newArea = $(area).clone();
                            // 身份资别
                            newArea.addClass('gallery-item-area-clone');
                            newArea.attr('galleryItemSerial', item.uuid);
                            // 寻找里面的元素
                            var replacer = function (text) {
                                if (!text)
                                    return text;
                                text = text.replace(/!\{title}/g, item.name);
                                text = text.replace(/!\{serial}/g, item.serial);
                                text = text.replace(/!\{src}/g, item.thumbnailUrl);
                                text = text.replace(/!\{url}/g, item.thumbnailUrl);
                                return text;
                            };

                            newArea.contents().each(function (index, ele) {
                                var eleJ = $(ele);
                                // console.log(ele, eleJ.text(), ele.attributes);
                                eleJ.text(replacer(eleJ.text()));
                                if (ele.attributes) {
                                    $.each(ele.attributes, function () {
                                        if (this.specified) {
                                            eleJ.attr(this.name, replacer(this.value));
                                        }
                                    });
                                }
                            });

                            // 添加到原位置
                            newArea.insertBefore(area);
                            newArea.show();
                        });
                    }
                });

            }
        });
    }

};


/**
 * 初始化root区域的自动find
 * @param root 一个jQuery结果
 * @param config 配置信息
 */
CRF.init = function (root, config) {
    root = root || $(document);
    config = config || {};

    var configGetter = function (ele, name) {
        if (config[name])
            return config[name];
        return ele.attr('data-' + name);
    };

    $(CRF.cssSelector, root).each(function (index, data) {
        // 这里应该有很多的处理层次
        // 如果定义了render 直接调用render完事儿(this)
        var _data = $(data);

        // 解析具体类型
        var dataClass = CRF.interception(_data.attr('class'));
        var classes = dataClass.split("-");
        var resourceName = classes[0];
        var title = "内容修改";
        var fixedType = null;

        if (classes[1] == "category") {
            title = "数据源修改";
            fixedType = classes[0];
            resourceName = classes[1];
        }

        var editInUrl = CRF.editInURI(resourceName, fixedType);

        data._store = {};
        data._store.root = root;
        data._store.resourceName = resourceName;
        data._store.title = title;
        data._store.fixedType = fixedType;
        data._store.editInURI = editInUrl;
        data._store.render = configGetter(_data, 'render');
        data._store.change = configGetter(_data, 'change');
        data._store.serial = configGetter(_data, 'serial');
        data._store.name = configGetter(_data, 'name');

        CRF.buildHtml(data, resourceName);
        // CMSWidgets.plugins.properties.util.getEleName(data);
        // var value = CMSWidgets.plugins.properties.util.getValueByProperties(CMSWidgets.plugins.properties.title);

        //绑定单击事件
        _data.on('click', function () {
            CRF.currentObject = this;

            CRF.iframeOpenId = layer.open({
                shadeClose: true,
                type: 2,
                title: this._store.title,
                area: ['70%', '80%'],
                content: this._store.editInURI
            });
        });

    });

};

$(function () {
    // 干好它该干的事儿
    $(document).on('content-changed', function (event, row) {
        //执行change
        CRF.exec(CRF.currentObject._store.change, CRF.currentObject, row);
        CRF.buildHtml(CRF.currentObject, CRF.currentObject._store.resourceName, row.serial);
        layer.close(CRF.iframeOpenId);
    });

    CRF.init();

    if (CRF.ready) {
        CRF.ready();
    }
});