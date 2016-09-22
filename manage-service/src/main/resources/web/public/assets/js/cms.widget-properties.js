/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

/**
 * 提供尽量简单的方式,让用户更改widget-properties
 * Created by CJ on 9/16/16.
 */

/**
 * http://docs.huobanplus.com/cms/staging/editor.html
 */

CMSWidgets.plugins.properties = {};
CMSWidgets.plugins.properties.util = {};//一些工具方法
CMSWidgets.plugins.properties.title = null;
CMSWidgets.plugins.properties.resourceName = null;
CMSWidgets.plugins.properties.globalId = null;
CMSWidgets.plugins.properties.iframeOpenId = null;//用户iframe关闭的标记
CMSWidgets.plugins.properties.data = {};


$(function () {
    CMSWidgets.plugins.properties.bindIframeEvent();
});

/**
 * 根据序列号生成元素里面的html代码
 * @param ele           元素
 * @param resourceName  资源名称
 * @param serial        序列号
 */
CMSWidgets.plugins.properties.buildHtml = function (ele, resourceName,serial) {
    var contentHTML = '<div><button class="js-addEditBtn btn btn-default" type="button">暂无数据</button></div>';
    if (serial != undefined) {
        //ajax请求：返回html代码展现在制定元素中去
        contentHTML=CMSWidgets.plugins.properties.contentHTML(resourceName,serial);
    }
    $(ele).html(contentHTML);
};

/**
 * 在doucment上绑定一个事件，用来监听iframe内的动作，一旦触发就根据回调的数据保存
 */
CMSWidgets.plugins.properties.bindIframeEvent = function () {
    $(document).on('content-changed', function (event, row) {
        //保存
        var properties = CMSWidgets.currentEditorProperties();
        properties[CMSWidgets.plugins.properties.title] = row.serial;
        //修改显示
        CMSWidgets.plugins.properties.buildHtml(CMSWidgets.plugins.properties.data,
                                                CMSWidgets.plugins.properties.resourceName,
                                                row.serial);
        layer.close(CMSWidgets.plugins.properties.iframeOpenId);
    });
};


/**
 * 根据资源类型和序列号以及站点ID生成指定url将返回的html渲染到指定元素中去
 * @param resourceName  资源类型
 * @param serial        序列号
 * @param ele           指定元素
 */
CMSWidgets.plugins.properties.contentHTML=function(resourceName,serial){
    //var url="/manage/"+resourceName+"/render";
    var url= CMSWidgets.contentURI(resourceName);
    if(url=='../view/testcontent.html'){
        return '<img src="https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/logo_white_fe6da1ec.png"/> ';
    }
    $.ajax({
        type: "GET",
        url:url,
        dataType:"html",
        data:{siteId:CMSWidgets.siteId,serial:serial},// 要提交的表单
        success: function(result) {
            return result;
        },
        error:function(e){
            layer.msg("获取内容失败");
        }
    });


}

/**
 * 工具函数，判断A字符串是否以B字符串结尾
 * @param str       A字符串
 * @param endStr    B字符串
 * @returns {boolean}
 */
CMSWidgets.plugins.properties.util.endWith = function (str, endStr) {
    var d = str.length - endStr.length;
    return (d >= 0 && str.lastIndexOf(endStr) == d)
};

/**
 * 工具函数,从给定的字符串中按照正则表达式截取指定的字符串
 * @param str
 */
CMSWidgets.plugins.properties.util.interception = function (str) {
    var pattern = /((article|gallery|link|notice)-(content|category))/g;
    var matching = pattern.exec(str);
    return matching[0];
};

/**
 * 将name作为key在properties关联数组中查找value值
 * @param name      key
 * @returns {*}
 */
CMSWidgets.plugins.properties.util.getValueByProperties=function(name){
    var properties = widgetProperties(CMSWidgets.plugins.properties.globalId);
    return properties[name];
};

/**
 * 在元素中获取data-name属性的值，如果为undefined则获取name属性的值，
 * 并且赋值到CMSWidgets.plugins.properties.title中去，
 * 用来当作properties的key
 * @param ele
 * @returns {*}
 */
CMSWidgets.plugins.properties.util.getEleName=function(ele){
    CMSWidgets.plugins.properties.title = $(ele).attr('data-name');
    if (CMSWidgets.plugins.properties.title == undefined) {
        CMSWidgets.plugins.properties.title = $(ele).attr('name');
    }
};


/**
 * 当编辑器打开的时候调用的函数
 * @param globalId 组件的id
 * @param identity 控件识别符
 * @param editAreaElement 编辑器的jquery结果
 */
CMSWidgets.plugins.properties.open = function (globalId, identity, editAreaElement) {
    CMSWidgets.plugins.properties.globalId = globalId;

    var editAreaElementMatch = "[class*='article-content'],[class*='gallery-content']," +
        "[class*='link-content'],[class*='notice-content']," +
        "[class*='article-category'],[class*='gallery-category']," +
        "[class*='link-category'],[class*='notice-category']";
    $(editAreaElementMatch, editAreaElement).each(function (index, data) {
        CMSWidgets.plugins.properties.util.getEleName(data);

        var value=CMSWidgets.plugins.properties.util.getValueByProperties(CMSWidgets.plugins.properties.title);

        //构建编辑器html代码
        CMSWidgets.plugins.properties.buildHtml(data, value);

        //绑定单击事件
        $(data).on('click', function () {
            var iframePath;
            var iframeTitle = "内容修改";
            var dataClass = CMSWidgets.plugins.properties.util.interception($(data).attr('class'));
            var strs = dataClass.split("-");

            var resourceName=strs[0];
            var fixedType=null;

            if(strs[1]=="category"){
                iframeTitle="数据源修改";
                fixedType=strs[0];
                resourceName=strs[1];
            }

            iframePath=CMSWidgets.editInURI(resourceName,fixedType);

            CMSWidgets.plugins.properties.data = this;
            CMSWidgets.plugins.properties.resourceName=strs[0];

            CMSWidgets.plugins.properties.iframeOpenId = layer.open({
                shadeClose: true,
                type: 2,
                title: iframeTitle,
                area: ['70%', '80%'],
                content: iframePath
            });
        });

        var galleryItemAreaMatch="[class*='gallery-item-area'][data-name='"+CMSWidgets.plugins.properties.title+"']";
        var galleryItemArea= $(galleryItemAreaMatch,editAreaElement);
        if(galleryItemArea.size()>0){
            //获取items

            //隐藏原始元素

            //循环输出元素

            console.log("有");


        }




    });




};




