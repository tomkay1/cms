define(function (require, exports, module) {
    require.async("widgetPageModel", function (a) {//页面对象处理->widgetPage
        var widgetPage = a.widgetPage();
        var widgetPageModel = widgetPage.getInstance();//页面持久化对象,后面根据这个对象系列化传递到后台,并创建对应的xml 配置文件
        var common=require("common");
        $.get("/assets/widget/toobar/toobar.html?t=66", function (html) {
            var divObj = document.createElement("div");
            divObj.innerHTML = html;
            divObj.id='js-page-toobar';
            var first = document.body.firstChild;//得到页面的第一个元素
            document.body.insertBefore(divObj, first);//在得到的第一个元素之前插入
            page.pageTab();
            page.pagePhoto();
            $("#tab1").addClass("current");
            page.pageProperty(widgetPageModel);
            page.pageSave(widgetPageModel);
            page.widgetAdd();
            page.layoutAdd();
            page.getWebRoot();
            page.widgetEdit();
            page.initPageObject();
            page.pageColor();
            layoutModule.initLayoutBind();
        })
        var layer = require("layer");
        var common = require("common");
        var customerId = common.getQuery("customerid");
        var siteId = common.getQuery("siteId");
        var configName = common.getQuery("config");
        var page = {
            pageTab: function () {
                var obj = $(".js-page-tab");
                $.each(obj, function (item, dom) {
                    $(dom).click(function () {
                        var id = $(dom).attr('id');
                        switch (id) {
                            case "tab1":
                                $(".js-page-tab").removeClass("current");
                                $(".js-layout").show();
                                $(dom).addClass("current");
                                $("#tab-box-2").hide();
                                $("#tab-box-3").hide();
                                break;
                            case "tab2":
                                $(".js-page-tab").removeClass("current");
                                $("#tab-box-2").show();
                                $(".js-layout").hide();
                                $("#tab-box-3").hide();
                                $(dom).addClass("current");
                                break;
                            case "tab3":
                                $(".js-page-tab").removeClass("current");
                                $("#tab-box-3").show();
                                $(".js-layout").hide();
                                $("#tab-box-2").hide();
                                $(dom).addClass("current");
                                break;
                        }
                    });
                });
            },
            pageTabNumber: function (number) {
                var obj = $(".js-page-tab");
                $.each(obj, function (item, dom) {
                    if (item == number) {
                        var id = $(dom).attr('id');
                        switch (id) {
                            case "tab1":
                                $(".js-page-tab").removeClass("current");
                                $(".js-layout").show();
                                $(dom).addClass("current");
                                $("#tab-box-2").hide();
                                $("#tab-box-3").hide();
                                break;
                            case "tab2":
                                $(".js-page-tab").removeClass("current");
                                $("#tab-box-2").show();
                                $(".js-layout").hide();
                                $("#tab-box-3").hide();
                                $(dom).addClass("current");
                                break;
                            case "tab3":
                                $(".js-page-tab").removeClass("current");
                                $("#tab-box-3").show();
                                $(".js-layout").hide();
                                $("#tab-box-2").hide();
                                $(dom).addClass("current");
                                break;
                        }
                    }
                });
            },
            pagePhoto: function () {
                $("#js-cms-selectPhoto").click(function () {
                    layer.open({
                        type: 2,
                        title: "图片库",
                        shadeClose: true,
                        shade: 0.8,
                        closeBtn: 1,
                        area: ['700px', '580px'],
                        content: "/assets/js/jPicture/photo.html?customerId=" + customerId + "&isMult=false&v=1.2",
                        //btn:["确定"],
                        end: function (index, layero) {
                            var jsonStr = $("#js_cms_picture_value").val();
                            var obj = JSON.parse(jsonStr);
                            if (typeof obj !== "undefined") {
                                for (var i = 0; i < obj.length; i++) {
                                    $("#pageBackImage").val(obj[i].localUri);
                                }
                            }
                        }
                    });
                });
            },
            pageColor: function () {
                require.async("widgetColor", function (b) {
                    b.pageColor();
                })
            },
            pageProperty: function (widgetPageModel) {
                var obj = $('.js-cms-submit');
                $.each(obj, function (item, dom) {
                    $(dom).click(function () {
                        var formId = $(dom).data('form');
                        var json = $("#" + formId).serializeJson();
                        widgetPageModel = widgetPage.setModel(json);
                        page.pageTabNumber(0);
                    });
                })
            },
            pageSave: function (widgetPage) {
                var obj = $(".js-page-create");
                $.each(obj, function (item, dom) {
                    $(dom).click(function () {
                        var publish = $(dom).data('publish');
                        var pageId = $("meta[name='exists']").attr('content');
                        if (pageId <= 0) {
                            page.createPage(widgetPage, publish);
                        } else {
                            page.patchPage(widgetPage, publish, pageId);
                        }
                    })
                })
            },
            createPage: function (widgetPage,publish) {
                widgetPage.layout=JQueue.toLayoutList();
                $.ajax({
                    type: "post",
                    dataType: "json",
                    url: '/page/createPage',//提交到一般处理程序请求数据
                    data: {
                        widgetStr: JSON.stringify(widgetPage),
                        customerId: customerId,
                        siteId: siteId,
                        publish: publish,
                        config: configName
                    },
                    success: function (data) {
                        if (data != null) {
                            if (data.code == "200") {
                                layer.msg("页面发布成功", {
                                    icon: 1,
                                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                                });
                            } else {
                                layer.msg("页面发布失败", {
                                    icon: 2,
                                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                                });
                            }
                        }
                    }
                })
            },
            patchPage: function (widgetPage, publish, pageId) {
                widgetPage.layout=JQueue.toLayoutList();
                $.ajax({
                    type: "post",
                    dataType: "json",
                    url: '/page/patch',//提交到一般处理程序请求数据
                    data: {
                        widgetStr: JSON.stringify(widgetPage),
                        customerId: customerId,
                        publish: publish,
                        id: pageId
                    },
                    success: function (data) {
                        if (data != null) {
                            if (data.code == "200") {
                                layer.msg("页面发布成功", {
                                    icon: 1,
                                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                                });
                            } else {
                                layer.msg("页面发布失败", {
                                    icon: 2,
                                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                                });
                            }
                        }
                    }
                })
            },
            widgetAdd: function () {
                var obj = $(".js-module-add");
                $(".js-module-add").unbind("click");
                $.each(obj, function (item, dom) {
                    var layoutId=$(dom).data('id');
                    var layoutPositionIndex=$(dom).data("index");
                    $(dom).click(function () {
                        widgetData.clear();
                        layer.open({
                            type: 2,
                            title: "组件模块",
                            shadeClose: true,
                            shade: 0.8,
                            closeBtn: 1,
                            area: ['700px', '580px'],
                            content: "/widget/widgetList?layoutId="+layoutId+"&index="+layoutPositionIndex,
                            //btn:["确定"],
                            end: function (index, layero) {
                                var widgetJson = $("#js_widget_json_value").html();
                                if (widgetJson == "404") {
                                    layer.msg("控件主体没有添加预览视图");
                                    return;
                                } else if (widgetJson == "500") {
                                    layer.msg("服务器错误,请稍候再试");
                                    return;
                                } else {
                                    var widgetObj = JSON.parse(widgetJson);
                                    var template = decodeURI(widgetObj.template);
                                    $(dom).before(template);
                                    delete widgetObj["template"];//对json对象进行删除template模版数据
                                    var layoutId = $(dom).data("id");//布局ID
                                    var layoutPosition = $(dom).data("index");//布局位置
                                    widgetObj.layoutId = layoutId;
                                    widgetObj.layoutPosition = layoutPosition;
                                    //window.console.log("layoutId--->"+layoutId+"  layoutPosition-->"+layoutPosition);
                                    //window.console.log(widgetObj);
                                    JQueue.putQueueLayoutWidget(layoutId, layoutPosition, widgetObj);
                                    page.widgetEdit();
                                }
                            }
                        });
                    });
                });
            },
            layoutAdd: function () {
                var obj = $(".js-layout-add");
                $.each(obj, function (item, dom) {
                    $(dom).click(function () {
                        widgetData.clear();
                        layer.open({
                            type: 2,
                            title: "添加布局",
                            shadeClose: true,
                            shade: 0.8,
                            closeBtn: 1,
                            area: ['600px', '480px'],
                            content: "/assets/widget/layout.html?v=1.42",
                            end: function (index, layero) {
                                page.widgetAdd();
                                var layoutJson=$("#js_layout_json_value").val();
                                var layoutObj=JSON.parse(layoutJson);
                                JQueue.putQueue(layoutObj);
                                layoutModule.initLayoutBind();
                            }
                        });
                    });
                });
            },
            getWebRoot:function(){
                $.ajax({
                    type: "post",
                    dataType: "json",
                    url: '/page/root',//提交到一般处理程序请求数据
                    data: {
                        siteId: siteId
                    },
                    success: function (data) {
                        if(data!=null){
                            if(data.code==200){
                                var urlRoot=data.data;
                                if(common.isDebug()=="1"){//调试模式
                                    urlRoot=urlRoot+":8080/front/";
                                }
                                $("#js-web-root").val(urlRoot);
                                $("#jq-web-home").attr("href",urlRoot);
                            }
                        }
                    }
                });
            },
            widgetEdit:function(){
                var obj=$(".js-widget-edit");
                obj.unbind("click");
                $.each(obj,function(item,dom){
                    $(dom).click(function(){
                        var widgetId=$(dom).data("id");
                        var layoutId=$(dom).data("layoutid");
                        var layoutPositionIndex=$(dom).data("position");
                        if(typeof layoutId=="undefined"||typeof layoutPositionIndex=='undefined'){
                            layer.msg("控件主体没有具备的布局信息标签[data-layoutid][data-position]");
                            return;
                        }
                        //window.console.log("layoutId-->"+layoutId+" layoutPositionIndex-->"+layoutPositionIndex+" widgetId-->"+widgetId);
                        var widget=JQueue.findLayoutWdigetByPositionAndWidgetId(layoutId,layoutPositionIndex,widgetId);//查找队列中改布局下的控件主体对象
                        //window.console.log(widget);
                        var json=JSON.stringify(widget);
                        widgetData.saveTempWidget(json);//把当前的控件主体的配置信息保存到临时隐藏域中
                        layer.open({
                            type: 2,
                            title: "设置",
                            shadeClose: true,
                            shade: 0.8,
                            closeBtn: 1,
                            area: ['700px', '580px'],
                            content: '/assets/widget/widgetEdit.html?widgetId='+widgetId+"&t="+Math.random(),//提交到一般处理程序请求数据
                            end: function (index, layero) {
                                var widgetSettingJson=$("#js_widgetSetting_json_value").val();//获得控件主体设置信息
                                var widgetSettingObj=JSON.parse(widgetSettingJson);
                                window.console.log(JQueue.toJson())
                                window.console.log("layoutId-->"+layoutId+"  layoutPostion-->"+layoutPositionIndex+"  widgetId-->"+widgetId);
                                var widget=JQueue.findLayoutWdigetByPositionAndWidgetId(layoutId,layoutPositionIndex,widgetId);//查找队列中改布局下的控件主体对象
                                if(widget==-1){
                                    layer.msg("没有找到控件主体信息");
                                    return;
                                }
                                widget.property=widgetSettingObj;//设置该控件主体下的设置信息
                                //window.console.log(widget);
                                JQueue.patchQueueLayoutWidget(widget)//修改该控件主体到队列中
                                //window.console.log(widgetSettingJson);
                                widgetModule.getWidgetBrief(widgetId,layoutId,layoutPositionIndex,widgetSettingJson);//获得控件主体预览视图
                            }
                        });
                    })
                })
            },
            /**
             * 加载页面配置数据（修改页面时有效）
             * */
            initPageObject:function(){
                var widgetPageData=common.getMetaContent("widget");
                $.ajax({
                    type: "post",
                    dataType: "json",
                    url: '/common/getWidgetPageData',//组件模版编辑预览视图解析
                    data: {
                        widget: widgetPageData
                    },
                    success: function (data) {
                        if(data!=null) {
                            if (data.code == 200 && data.data != null) {
                                var obj = JSON.parse(data.data);
                                if(obj!=null){
                                    if (obj.layout != null) {
                                        JQueue.putQueueList(obj.layout);//把页面中的配置信息初始化到JQueue队列中
                                        delete obj["layout"];
                                    }
                                    widgetPageModel = widgetPage.setModel(obj);
                                }
                                page.initPageProperty();
                            } else {
                                layer.msg("加载页面配置数据失败...");
                            }
                        }
                    },
                    error:function(){
                        layer.msg("加载页面配置数据失败...");
                    }
                });
            },
            initPageProperty:function(){
                if(widgetPageModel!=null) {
                    $("#pageName").val(widgetPageModel.pageName == null ? "" : widgetPageModel.pageName);
                    $("#pageKeyWords").val(widgetPageModel.pageKeyWords == null ? "" : widgetPageModel.pageKeyWords);
                    $("#pageDescription").val(widgetPageModel.pageDescription == null ? "" : widgetPageModel.pageDescription);

                    $("#url").val(widgetPageModel.url);
                    $("#pageBackGround").attr("data-default",(widgetPageModel.pageBackGround == null ? "#ffffff" : widgetPageModel.pageBackGround));

                    $("#pageBackImage").val(widgetPageModel.pageBackImage == null ? "" : widgetPageModel.pageBackImage);
                    $("#pageBackRepeat").find("option[text='" + (widgetPageModel.pageBackRepeat == null ? "" : widgetPageModel.pageBackRepeat) + "']").attr("selected", true);
                    $("#pageBackAlign").find("option[text='" + (widgetPageModel.pageBackAlign == null ? "" : widgetPageModel.pageBackAlign) + "']").attr("selected", true);
                    $("#pageBackVertical").find("option[text='" + (widgetPageModel.pageBackVertical == null ? "" : widgetPageModel.pageBackVertical) + "']").attr("selected", true);

                    $("#pageEnableHead").attr("checked",(widgetPageModel.pageEnabledHead == null ? "false" : widgetPageModel.pageEnabledHead));
                    if(configName!="head"){
                        page.pageEnableHead();
                    }else{
                        $("#jq-page-common-no").hide();
                    }
                }
            },
            pageEnableHead:function(){
                page.pageInitHeader();
                $("#pageEnableHead").click(function(){
                    var pageEnabledHead=$("#pageEnableHead").is(':checked');
                    widgetPageModel.pageEnabledHead =pageEnabledHead;
                    widgetPageModel= widgetPage.setModel(widgetPageModel);
                    if(pageEnabledHead.toString()=='true'){
                        page.pageInitHeader();
                    }else{
                        $(".js-layout-header").remove();
                    }
                });
            },
            pageInitHeader:function(){
                var pageEnabledHead=$("#pageEnableHead").is(':checked');
                if(pageEnabledHead.toString()=='true'&&configName!="head"){
                    $.ajax({
                        type: "post",
                        dataType: "json",
                        url: '/widgetTemplate/common/head',//组件模版编辑预览视图解析
                        data: {
                            siteId:siteId,
                        },
                        success: function (data) {
                            if(data!=null){
                                if(data.code==200){
                                    var dom=$(data.data);
                                    dom.removeClass("js-hot-layout");
                                    dom.addClass("js-layout-header");
                                    $("#js-page-toobar").after(dom);
                                }else if(data.code==404){
                                    layer.msg("没有设置公共头部");
                                }else{
                                    layer.msg("服务器繁忙,请稍后再试...");
                                }
                            }else{
                                layer.msg("服务器繁忙,请稍后再试...");
                            }
                        },error:function(){
                            layer.msg("服务器繁忙,请稍后再试...");
                        }
                    });
                }
            }
        };
        var widgetModule={
            getWidgetBrief:function(widgetId,layoutId,positionIndex,settingString){
                $.ajax({
                    type: "post",
                    dataType: "json",
                    url: '/widgetTemplate/' + widgetId,//组件模版编辑预览视图解析
                    data: {
                        properties:settingString,
                        layoutId:layoutId,
                        layoutPosition:positionIndex
                    },
                    success: function (data) {
                        if(data!=null){
                            if(data.code==200){
                                $("#"+widgetId).replaceWith(data.data);
                                page.widgetEdit();
                            }else{
                                layer.msg("解析模版错误");
                            }
                        }else{
                            layer.msg("解析模版错误");
                        }
                    },
                    error:function(){
                        layer.msg("系统或者网络繁忙,请稍候再试...");
                    }
                });
            }
        };
        var layoutModule={
            initLayoutBind:function(){
                layoutModule.deleteLayout();
                layoutModule.downLayout();
                layoutModule.upLayout();
            },
            deleteLayout:function(){
                var obj=$(".js-layout-delete");
                obj.unbind("click");
                $.each(obj,function(item,dom){
                    $(dom).click(function(){
                        var layoutId=$(dom).data('id');
                        $("#"+layoutId).remove();
                        JQueue.delete(layoutId);
                    });
                });
            },
            upLayout:function(){
                var obj=$(".js-layout-up");
                obj.unbind("click");
                $.each(obj,function(item,dom){
                   $(dom).click(function(){
                       var layoutId=$(dom).data('id');
                       var $this=$("#"+layoutId);
                       if ($this.prev() && $this.prev().length>0) {
                           if($this.prev().hasClass('js-hot-layout')){
                               $this.prev().before($this);
                           }
                       }
                   }) ;
                });
            },
            downLayout:function(){
                var obj=$(".js-layout-down");
                obj.unbind("click");
                $.each(obj,function(item,dom){
                   $(dom).click(function(){
                       var layoutId=$(dom).data('id');
                       var $this=$("#"+layoutId);
                       if ($this.next()&&$this.next().length>0) {
                           if($this.next().hasClass('js-hot-layout')){
                               $this.next().after($this);
                           }
                       }
                   });
                });
            }
        };
    })
});