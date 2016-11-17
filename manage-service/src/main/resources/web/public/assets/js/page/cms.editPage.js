
var editFunc = {
    handleWidgetChildrenIds: function (id, data) {
        var t = editFunc.randomNumber();
        switch (id) {
            case 'navbar':
                data.e.find('button.navbar-toggle').attr('data-target', '#navList' + t);
                data.e.find('div.navbar-collapse').attr('id', '#navList' + t);
                break;
        }
    },
    handleWidgetIds: function(id) {
        var data = editFunc.getWidgetId(id);
        editFunc.handleWidgetChildrenIds(id, data);
        data.e.attr("id", data.n);
        GlobalID = data.n;
        widgetHandle.createStore(GlobalID);
    },
    getWidgetId: function(id) {
        var data = {};
        var t = editFunc.randomNumber();
        data.e = $('.pageHTML').find('#'+id);
        data.n = id + "-" + t;
        return data;
    },
    randomNumber: function() {
        return +new Date();
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
                $(this).parent().siblings('.drag').show()
            } else {
                $(this).parent().siblings('.drag').hide()
            }
        });
    },
    removeElement: function() {
        $(".pageHTML").on("click", ".remove", function (e) {
            e.preventDefault();
            var globalId = $(this).siblings('.view').children().attr('id');
            if (globalId) {
                wsCache.delete(globalId);
            } else {
                $(this).parent().find('.view').each(function (i, v) {
                    var id = $(v).children().attr('id');
                    if(id) {
                        wsCache.delete(id);
                    }
                });
            }
            
            $(this).parent().remove();

        });
    },
    settingElement: function () {
        $('.pageHTML').on("click", ".box .setting", function () {
            $('.mask-backdrop').show();
            var ele = $('#configuration');
            ele.show();
            var id = $(this).attr('data-target');
            $('.conf-body').find('.common-conf').each(function () {
                var oId = $(this).attr('data-id') ;
                if (editFunc.contrastString(oId, id)) {
                    $(this).show();
                }
            });
            ele.stop().animate({
                right: 0
            }, 500);
            // 创建当前操作组件的数据
            widgetHandle.openEditor($(this));
            var element = $('#'+GlobalID);
            var styleid = element.attr('data-styleid');
            var container = editFunc.findCurrentEdit(GlobalID);
            container.find('img.changeStyle').each(function () {
                if ( $(this).attr('data-styleid') == styleid ) {
                    editFunc.changeImgStyleActive($(this));
                }
            });
        });
    },
    findCurrentEdit: function (elementId) {
        var element = $('#'+elementId);
        var widgetidentity = element.attr('data-widgetidentity');
        var container = '';
        $('.common-conf').each(function () {
            var oId = $(this).attr('data-id') ;
            if( editFunc.contrastString(oId, widgetidentity)) {
                container = $(this);
            }
        });
        return container;
    },
    contrastString: function (includeStr, targetStr) {
        return (targetStr.indexOf(includeStr) > -1);
    },
    saveConfig: function () {
        $('#confBtn').click(function () {
            widgetHandle.saveFunc(GlobalID);
        });
    },
    closeConfig: function () {
        $('#cancelBtn').click(function () {
            widgetHandle.closeSetting();
        });
    },
    closeFunc: function () {
        var ele = $('#configuration');
        var w = ele.width();
        ele.stop().animate({
            right: -w
        },500, function () {
            $('.common-conf').hide();
            $('#configuration').hide();
            $('.mask-backdrop').hide();
        });
    },
    clearDemo: function() {
        var cloneDOM = $('#js-global-setting');
        $(".pageHTML").empty().append(cloneDOM);
    },
    removeMenuClasses: function() {
        $(".operate-buttons li").removeClass("active")
    },
    changeImgStyleActive: function (ele) {
        $('img.changeStyle').parent().removeClass('active');
        ele.parent().addClass('active');
    },
    handleJsIds: function (id) {
        editFunc.handleWidgetIds(id);
    },
    closePreloader: function () {
        $('#status').fadeOut();
        $('#preloader').delay(350).fadeOut();
    },
    init: function () {
        editFunc.removeElement();
        editFunc.settingElement();
        editFunc.saveConfig();
        editFunc.closeConfig();
        editFunc.gridSystemGenerator();
    },
    dragFunc: function () {
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
            stop: function () {
                $(".pageHTML .column").sortable({
                    opacity: .35,
                    connectWith: ".column"
                });
            }
        });
    }
};
var Page = {
    widgetHTML: [
        '<div class="box box-element ui-draggable">',
        '<span class="setting label label-primary"><i class="fa fa-cog"></i> 设置</span>',
        '<span class="drag label label-default"><i class="fa fa-arrows"></i> 拖动</span>',
        '<span class="remove label label-danger"><i class="fa fa-times"></i> 删除</span>',
        '<div class="preview"><p></p></div>',
        '<div class="view"></div>',
        '</div>'
    ],
    styleList: [
	    '<div>',
	    '<h3><i class="fa fa-puzzle-piece"></i><strong>选择组件样式</strong></h3>',
	    '<div class="swiper-container styles">',
	    '<div class="swiper-wrapper">',
	    '</div>',
	    '<div class="swiper-button-next"></div>',
	    '<div class="swiper-button-prev"></div>',
	    '</div>',
	    '</div>'
	],
    createListAndEditor: function (data) {
        var parent = $('#configuration').find('.conf-body');
        var element = $('#widgetLists');
        var typeData = $.unique($.map(data, function (v) {return [$.trim(v.type)];}));
        $.each(typeData, function (i,v) {
            var typeList = $('<li class="widgetListGroup"></li>');
            var div = $('<div class="group-header clearfix"><i class="fa fa-list"></i>'+ v +'<i class="fa fa-chevron-left"></i></div>');
            var widgetList = $('<ul class="group-content list-unstyled"></ul>');
            typeList.append(div);
            typeList.append(widgetList);
            element.append(typeList);
        });
        $.each(data, function (i, v) {
            var initData = {};
            initData.script = v.scriptHref;
            initData.properties = v.defaultProperties;
            wsCache.set(v.identity, initData);
            // 组件列表渲染
            // 解决版本筛选问题
            if (v.flag === true) {
                var typeList = $('.group-header:contains("'+v.type+'")').map(function () {
                    if ($(this).text() == v.type ) {
                        return this;
                    }
                }).siblings('.group-content');
                var list = $('<li></li>');
                list.append(Page.widgetHTML.join('\n'));
                if( !v.styles[0].previewHTML ) {
                    var errorDiv = $('<div id="errorPlaceholder"></div>');
                    list.find('.setting').addClass('hidden');
                    errorDiv.html(v.styles[0].previewFailed)
                }
                list.find('.setting').attr('data-target', v.identity);
                list.find('.preview p').html(v.locallyName);

                list.find('.view').append(v.styles[0].previewHTML || errorDiv);
                list.find('.view').children().eq(0).attr('data-widgetidentity', v.identity);
                list.find('.view').children().eq(0).attr('data-styleid', v.styles[0].id);
                typeList.append(list);
                //编辑器视图渲染
                var child = $('<div class="common-conf"></div>');
                var container = $('<div></div>');
                var h3 = $('<h3><i class="fa fa-puzzle-piece"></i><strong>设置组件参数</strong></h3>');
                //解决版本号不匹配问题
                var widgetidentity = v.identity.split(':')[0];
                child.attr('data-id', widgetidentity);
                child.append(Page.styleList.join('\n'));
                $.each(v.styles, function (key, val) {
                    var div = $('<div class="swiper-slide"></div>');
                    var img = $('<img class="center-block changeStyle">');
                    var p = $('<p></p>');
                    var span = $('<span class="theme-thumb-magnifier"><b class="fa fa-search-plus" aria-hidden="true"></b></span>');
                    img.attr('src',val.thumbnail);
                    img.attr('data-styleid',val.id);
                    p.text(val.locallyName);
                    div.append(img);
                    div.append(p);
                    div.append(span);
                    child.find('.swiper-wrapper').append(div);
                });
                container.append(h3);
                child.append(container);
                parent.append(child);
            }


            $('.swiper-container.styles').swiper({
                nextButton: '.swiper-button-next',
                prevButton: '.swiper-button-prev',
                slidesPerView: 4,
                paginationClickable: true,
                observer: true,
                observeParents: true,
                updateOnImagesReady : true
            });
        });
        Page.draggable();
    },
    init: function (url) {
        $.ajax({
            type: 'GET',
            url: url + "?pageType=" + pageType,
            dataType: 'json',
            statusCode: {
                403: function() {
                    layer.msg('没有权限', {time: 2000});
                    editFunc.closePreloader();
                },
                404: function() {
                    layer.msg('页面请求失败', {time: 2000});
                    editFunc.closePreloader();
                },
                502: function () {
                    layer.msg('服务器错误,请稍后再试', {time: 2000});
                    editFunc.closePreloader();
                }
            },
            success: function (result) {
                if(result) editFunc.closePreloader();
                Page.createListAndEditor(result);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log(errorThrown);
                layer.msg('服务器错误,请稍后再试', {time: 2000});
            }
        });
    },
    draggable: function () {
        $(".draggable-group .box").draggable({
            connectToSortable: ".column",
            helper: "clone",
            handle: ".drag",
            create: function(e, ui ) {
                var ele = $(e.target).find('.view').children().eq(0);
                var oId = ele.attr('id');
                if ( !oId ) {
                    ele.attr('id', Page.randomId(6))
                }
            },
            drag: function (e, t) {
                t.helper.width(400);
            },
            stop: function (e, t) {
                var oId = t.helper.find('.view').children().eq(0).attr('id');
                editFunc.handleJsIds(oId);
            }
        });
    },
    randomId: function (num) {
        var str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        var s = '';
        for(var i = 0; i < num; i++){
            var rand = Math.floor(Math.random() * str.length);
            s += str.charAt(rand);
        }
        return s;
    }
};

var editPage = {};

editPage.init = function () {
    $(document.body).css("min-height", $(window).height() - 90);
    $(".pageHTML").css("min-height", $(window).height() - 160);

    editFunc.dragFunc();

    // $("#editBtn").click(function () {
    //     $(document.body).removeClass("sourcepreview");
    //     $(document.body).addClass("edit");
    //     editFunc.removeMenuClasses();
    //     $(this).addClass("active");
    //     $('.edit').find('.sidebar').show();
    //     return false;
    // });
    //
    // $("#previewBtn").click(function () {
    //     if ( $('#pageHTML').html() === '' ) return false;
    //     $(document.body).removeClass("edit");
    //     $(document.body).addClass("sourcepreview");
    //     editFunc.removeMenuClasses();
    //     $(this).addClass("active");
    //     $('.sourcepreview').find('.sidebar').hide();
    //     return false;
    // });

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

    $('.conf-body').on('click','img.changeStyle', function () {
        editFunc.changeImgStyleActive($(this));
        var id = $(this).attr('data-styleid');
        $('#'+GlobalID).attr('data-styleid',id);
    });


    $('div[id^="picCarousel"]').swiper({
        pagination: '.swiper-pagination',
        autoplay : 5000,
        slidesPerView: 1,
        paginationClickable: true,
        observer: true,
        observeParents: true,
        updateOnImagesReady : true,
        loop: true
    });
    // 导致死循环的BUG
    // $('.boxes').niceScroll({
    //     cursorcolor:"#fff",
    //     cursoropacitymax: 0.2
    // });

    $('#widgetLists').on('click', '.group-header', function () {
        $(this).parent().toggleClass('active');
        $(this).children('.fa-chevron-left').toggleClass('fa-chevron-down');
        $(this).siblings('.group-content').slideToggle();

        $(this).parent().siblings().removeClass('active');
        $(this).parent().siblings().find('.group-header .fa-chevron-left').removeClass('fa-chevron-down');
        $(this).parent().siblings().find('.group-content').slideUp();
    });

    $('#configuration').on('click', '.theme-thumb-magnifier', function () {
       var img = $(this).siblings('img').attr('src');
        layer.open({
            type: 1,
            title: '预览图',
            area: '800px', //宽高
            content: '<img style="padding: 20px; max-width: 780px; margin: 0 auto; " src="'+ img +'">'
        });
    });

    $('#pageHTML').on('click', 'a', function () {
        return false;
    });

    editFunc.init();
    Page.init(initPath);
};

editPage.init();



