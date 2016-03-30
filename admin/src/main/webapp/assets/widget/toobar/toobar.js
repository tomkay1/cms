define(function (require, exports, module) {
    require.async("widgetPageModel", function (a) {//页面对象处理->widgetPage
        var widgetPage = a.widgetPage();
        var widgetPageModel = widgetPage.getInstance();//页面持久化对象,后面根据这个对象系列化传递到后台,并创建对应的xml 配置文件
        $.get("/assets/widget/toobar/toobar.html?t=32", function (html) {
            var divObj = document.createElement("div");
            divObj.innerHTML = html;
            var first = document.body.firstChild;//得到页面的第一个元素
            document.body.insertBefore(divObj, first);//在得到的第一个元素之前插入
            page.pageTab();
            page.pagePhoto();
            page.pageColor();
            $("#tab1").addClass("current");
            page.pageProperty(widgetPageModel);
            page.pageSave(widgetPageModel);
            page.widgetAdd();
            page.layoutAdd(widgetPageModel);
            page.getWebRoot();
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
            createPage: function (widgetPage, publish) {
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
                    $(dom).click(function () {
                        layer.open({
                            type: 2,
                            title: "组件模块",
                            shadeClose: true,
                            shade: 0.8,
                            closeBtn: 1,
                            area: ['700px', '580px'],
                            content: "/widget/widgetList?v=1.2",
                            //btn:["确定"],
                            end: function (index, layero) {
                                window.console.log($("#js_widget_template_value").val())
                                $(dom).before($("#js_widget_template_value").val());
                                //var jsonStr = $("#js_cms_picture_value").val();
                                //var obj = JSON.parse(jsonStr);
                                //if (typeof obj !== "undefined") {
                                //    for (var i = 0; i < obj.length; i++) {
                                //        $("#pageBackImage").val(obj[i].localUri);
                                //    }
                                //}
                            }
                        });
                    });
                });
            },
            layoutAdd: function (widgetPageModel) {
                var obj = $(".js-layout-add");
                $.each(obj, function (item, dom) {
                    $(dom).click(function () {
                        layer.open({
                            type: 2,
                            title: "添加布局",
                            shadeClose: true,
                            shade: 0.8,
                            closeBtn: 1,
                            area: ['600px', '480px'],
                            content: "/assets/widget/layout.html?v=1.3",
                            end: function (index, layero) {
                                page.widgetAdd();
                                var layoutTypeId = $("#js_layoutType_id_input_value").val();//页面布局id -->layout
                                var widgetLayout = a.widgetLayout();
                                var widgetLayoutModel = widgetLayout.getInstance();
                                widgetLayoutModel = widgetLayout.setLayoutType(layoutTypeId);
                                if (typeof widgetPageModel.layout == "undefined") {
                                    widgetPageModel = widgetPage.setWidgetLayout(widgetLayoutModel);
                                } else {
                                    widgetPageModel = widgetPage.pushWidgetLayout(widgetLayoutModel);
                                }
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
        }
    })
});