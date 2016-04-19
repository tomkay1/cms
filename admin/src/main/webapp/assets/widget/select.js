define(function (require, exports, module) {
    var layer = require("layer");
    var common=require("common");
    var layoutId=common.getQuery("layoutId");//所在布局ID
    var positionIndex=common.getQuery("index");//所在布局的位置index
    var siteId=common.getQuery("siteId");
    var widget = {
        widgetTab: function () {
            var obj = $(".js-widget-module");
            $.each(obj, function (item, dom) {
                $(dom).click(function () {
                    var cateId = $(dom).data("cateid");
                    $(".j-module-list ul").removeClass("show");
                    $(".js-widget-module").removeClass("show");
                    $("#my_modules_" + cateId).addClass("show");
                    $(dom).addClass("show");
                });
            })
        },
        widgetAdd: function () {
            var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
            var obj = $(".js-widget-add");
            $.each(obj, function (item, dom) {
                $(dom).click(function () {
                    var widgetUrl = $(dom).data("url");
                    var widgetId=$(dom).data("id");
                    $.ajax({
                        type: "post",
                        dataType: "json",
                        url: '/widgetTemplate/'+widgetId,//组件模版编辑预览视图解析
                        data: {
                            layoutId:layoutId,
                            layoutPosition:positionIndex,
                            siteId:siteId
                        },
                        success: function (data) {
                            if(data!=null){
                                if(data.code==200){
                                    var widgetGuid=data.data.guid;
                                    var template=encodeURI(data.data.html);
                                    //window.console.log(template);
                                    var widget={
                                        guid:widgetGuid,//用做唯一标识
                                        id:widgetId,//控件主体ID跟数据库对应,不能确定唯一
                                        widgetUri:widgetUrl,
                                        widgetEditUri:"",
                                        property:[],
                                        template:template
                                    }
                                    var widgetJson=JSON.stringify(widget)
                                    //window.console.log(widget);
                                    widgetData.saveWidget(widgetJson);
                                }else if(data.code==404){
                                    widgetData.saveWidget("404");
                                }else{
                                    widgetData.saveWidget("500");
                                }
                            }else{
                                widgetData.saveWidget("500");
                            }
                            parent.layer.close(index);
                        },
                        error:function(){
                            widgetData.saveWidget("500");
                            parent.layer.close(index);
                        }
                    });
                });
            });
        }
    }
    widget.widgetTab();
    widget.widgetAdd();
});