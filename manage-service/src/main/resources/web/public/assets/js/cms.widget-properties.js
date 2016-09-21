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
CMSWidgets.plugins.properties.util={};
CMSWidgets.plugins.properties.title=null;
CMSWidgets.plugins.properties.globalId=null;
CMSWidgets.plugins.properties.iframeSpan=null;
CMSWidgets.plugins.properties.iframeOpenId=null;
//title,iframeSpan,iframeOpenId
//CMSWidgets.plugins.properties.widgetProperties = {};


$(function(){
    CMSWidgets.plugins.properties.bindSpanIframeEvent();
});

/**
 * 根据序列号生成元素里面的html代码
 * @param ele       元素
 * @param serial    序列号
 */
CMSWidgets.plugins.properties.buildSpanHtml=function(ele,serial){
    var text="暂无数据";
    if(serial!=undefined){
        text="序列号:"+serial;
    }
    $(ele).html('<button class="js-addEditBtn btn btn-default" type="button">'+text+'</button>');
};

/**
 * 在doucment上绑定一个事件，用来监听iframe内的动作，一旦触发就根据回调的数据保存
 */
CMSWidgets.plugins.properties.bindSpanIframeEvent=function(){
    $(document).on('content-changed', function (event, row) {
        //保存
        var properties=widgetProperties(CMSWidgets.plugins.properties.globalId);
        properties[CMSWidgets.plugins.properties.title]=row.serial;
        //修改显示
        CMSWidgets.plugins.properties.buildSpanHtml(CMSWidgets.plugins.properties.iframeSpan,row);
        layer.close(CMSWidgets.plugins.properties.iframeOpenId);
    });
};


/**
 * 工具函数，判断A字符串是否以B字符串结尾
 * @param str       A字符串
 * @param endStr    B字符串
 * @returns {boolean}
 */
CMSWidgets.plugins.properties.util.endWith=function(str,endStr){
    var d=str.length-endStr.length;
    return (d>=0&&str.lastIndexOf(endStr)==d)
}


// 应该存在一个function名为 CMSWidgets.editInURI(resourceName,fixedType)
CMSWidgets.plugins.properties.open = function (globalId, identity, editAreaElement) {
    CMSWidgets.plugins.properties.globalId=globalId;
    //console.log('hello world', editAreaElement);

    //CMSWidgets.plugins.properties.widgetProperties = widgetProperties(globalId);
    var iframeOpenId;//layer 关闭iframe所需的标记
    var iframeSpan;


    $("[class$='-content'],[class$='-category']", editAreaElement).each(function (index, data) {
        var title=$(data).attr('data-name');
        if(title==undefined){
            title=$(data).attr('name');
        }
        var iframePath="";
        var iframeTitle="内容修改";
        var dataClass=$(data).attr('class');
        var strs=dataClass.split("-");
        iframePath="manage/"+strs[0]+"/editIn?siteId="+CMSWidgets.siteId;

        if(strs[1]=="category"){
            iframeTitle="数据源修改";
            iframePath=iframePath+"&fixedType="+strs[0];//确定参数
        }
        var properties=widgetProperties(CMSWidgets.plugins.properties.globalId);
        var value = properties[title];


        CMSWidgets.plugins.properties.buildSpanHtml(data,value);

        $(data).on('click',function(){
            CMSWidgets.plugins.properties.iframeSpan=this;

            CMSWidgets.plugins.properties.iframeOpenId=layer.open({
                closeBtn: 0,
                shadeClose: true,
                type: 2,
                title: iframeTitle,
                area: ['60%', '60%'],
                content:"iframetest.html",
                //content:iframePath,
            });
            //CMSWidgets.plugins.properties.bindSpanIframeEvent(title,iframeSpan,iframeOpenId);
        });
    });

};




