/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

    var editFunc = {
        handleSaveLayout: function () {
            var e = $(".pageHTML").html();
            if (e != window.demoHtml) {
                saveLayout();
                window.demoHtml = e;
            };
        },
        handleAccordionIds: function() {
            var e = $(".pageHTML #myAccordion");
            var t = editFunc.randomNumber();
            var n = "panel-" + t;
            var r;
            e.attr("id", n);
            e.find(".panel").each(function (e, t) {
                r = "panel-element-" + editFunc.randomNumber();
                $(t).find(".panel-title").each(function (e, t) {
                    $(t).attr("data-parent", "#" + n);
                    $(t).attr("href", "#" + r)
                });
                $(t).find(".panel-collapse").each(function (e, t) {
                    $(t).attr("id", r)
                })
            })
        },
        handleCarouselIds: function() {
            var e = $(".pageHTML #carousel");
            var t = editFunc.randomNumber();
            var n = "carousel-" + t;
            e.attr("id", n);
            e.find(".carousel-indicators li").each(function (e, t) {
                $(t).attr("data-target", "#" + n)
            });
            e.find(".left").attr("href", "#" + n);
            e.find(".right").attr("href", "#" + n)
        },
        handleModalIds: function() {
            var e = $(".pageHTML #myModalLink");
            var t = editFunc.randomNumber();
            var n = "modal-container-" + t;
            var r = "modal-" + t;
            e.attr("id", r);
            e.attr("href", "#" + n);
            e.next().attr("id", n)
        },
        handleTabsIds: function() {
            var e = $(".pageHTML #myTabs");
            var t = editFunc.randomNumber();
            var n = "tabs-" + t;
            e.attr("id", n);
            e.find(".tab-pane").each(function (e, t) {
                var n = $(t).attr("id");
                var r = "panel-" + editFunc.randomNumber();
                $(t).attr("id", r);
                $(t).parent().parent().find("a[href=#" + n + "]").attr("href", "#" + r)
            })
        },
        randomNumber: function() {
            return editFunc.randomFromInterval(1, 1e6)
        },
        randomFromInterval: function(e, t) {
            return Math.floor(Math.random() * (t - e + 1) + e)
        },
        gridSystemGenerator: function() {
            $(".ncrow .preview input").bind("keyup", function () {
                var e = 0;
                var t = "";
                var n = false;
                var r = $(this).val().split(" ", 12);
                $.each(r, function (r, i) {
                    if (!n) {
                        if (parseInt(i) <= 0) n = true;
                        e = e + parseInt(i);
                        t += '<div class="col-md-' + i + ' column"></div>'
                    }
                });
                if (e == 12 && !n) {
                    $(this).parent().next().children().attr('data-layout-value', r.join(','));
                    $(this).parent().next().children().html(t);
                    $(this).parent().prev().show()
                } else {
                    $(this).parent().prev().hide()
                }
            });
        },
        removeElement: function() {
            $(".pageHTML").on("click", ".remove", function (e) {
                e.preventDefault();
                $(this).parent().remove();
                if (!$(".pageHTML .ncrow").length > 0) {
                    editFunc.clearDemo();
                }
            });
        },
        settingElement: function () {
            $('.pageHTML').on("click", ".setting", function () {
                $('.modal-backdrop').fadeIn();
                $('#configuration').show();
                var id = $(this).data('target');
                $('#' + id).show();
                $('#configuration').stop().animate({
                    right: 0
                },500);
            });
        },
        closeConfig: function () {
            $('#cancelBtn').click(function () {
                var w = $('#configuration').width();
                $('#configuration').stop().animate({
                    right: -w
                },500, function () {
                    $('.common-conf').hide();
                    $('#configuration').hide();
                    $('.modal-backdrop').fadeOut();
                });
            });
        },
        clearDemo: function() {
            $(".pageHTML").empty();
        },
        removeMenuClasses: function() {
            $(".operate-buttons li").removeClass("active")
        },
        handleJsIds: function () {
            editFunc.handleModalIds();
            editFunc.handleAccordionIds();
            editFunc.handleCarouselIds();
            editFunc.handleTabsIds();
        },
        init: function () {
            editFunc.removeElement();
            editFunc.settingElement();
            editFunc.closeConfig();
            editFunc.gridSystemGenerator();
        }
    };
    var Page = {
        widgetHTML: [
            '<li>',
            '<div class="box box-element ui-draggable">',
            '<span class="setting label label-primary">',
            '<i class="icon-cog"></i> 设置',
            '</span>',
            '<span class="drag label label-default">',
            '<i class="icon-move"></i> 拖动',
            '</span>',
            '<span class="remove label label-danger">',
            '<i class="icon-cancel"></i> 删除',
            '</span>',
            '<div class="preview">',
            '<p></p>',
            '</div>',
            '<div class="view"></div>',
            '</div>',
            '</li>'
        ],
        init: function (url) {
            $.getJSON(url, function(result){
                $.each(result, function (i, v) {
                    $('#widgetLists').append(Page.widgetHTML.join(' '));
                    $('#widgetLists .setting').eq(i).attr('data-target',v.identity);
                    $('#widgetLists .preview p').eq(i).html(v.locallyName);
                    $('#widgetLists .view').eq(i).append(v.styles[0].previewHTML)
                });
                Page.draggable();
            });
        },
        draggable: function () {
            $(".draggable-group .box").draggable({
                connectToSortable: ".column",
                helper: "clone",
                handle: ".drag",
                drag: function (e, t) {
                    t.helper.width(400)
                },
                stop: function () {
                    editFunc.handleJsIds();
                }
            });
        }
    };
var editPage = {};
    editPage.init = function () {
        $(document.body).css("min-height", $(window).height() - 90);
        $(".pageHTML").css("min-height", $(window).height() - 160);
        $(".pageHTML, .pageHTML .column").sortable({
            connectWith: ".column",
            opacity: .35,
            handle: ".drag"
        });
        $(".draggable-group .ncrow").draggable({
            connectToSortable: ".pageHTML",
            helper: "clone",
            handle: ".drag",
            drag: function (e, t) {
                t.helper.width(400);
            },
            stop: function (e, t) {
                $(".pageHTML .column").sortable({
                    opacity: .35,
                    connectWith: ".column"
                });
            }
        });

        $("#editBtn").click(function () {
            $(document.body).removeClass("sourcepreview");
            $(document.body).addClass("edit");
            editFunc.removeMenuClasses();
            $(this).addClass("active");
            $('.edit').find('.sidebar').show();
            return false;
        });
        $("#previewBtn").click(function () {
            if ( $('#pageHTML').html() === '' ) return false;
            $(document.body).removeClass("edit");
            $(document.body).addClass("sourcepreview");
            editFunc.removeMenuClasses();
            $(this).addClass("active");
            $('.sourcepreview').find('.sidebar').hide();
            return false;
        });
        $("#clearBtn").click(function (e) {
            if ( $('#pageHTML').html() === '' ) return false;
            e.preventDefault();
            layer.confirm('页面会被清空？', {
                title: '警告',
                btn: ['重做','取消']
            }, function(index){
                editFunc.clearDemo();
                layer.close(index);
            });
        });

        editFunc.init();

        // 页面列表初始化
        Page.init('/manage/widget/widgets');
    };
editPage.init();
