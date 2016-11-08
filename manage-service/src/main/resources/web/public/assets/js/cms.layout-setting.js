/**
 * Created by Neo on 2016/10/27.
 */

/**
 * 属性编辑器的行为交互函数
 * @type {{
 * targetElement: null, 获取当前layout的设置按钮jQuery对象
 * openFunc: LayoutSetting.openFunc, 打开编辑器
 * closeFunc: LayoutSetting.closeFunc, 关闭编辑器
 * saveFunc: LayoutSetting.saveFunc, 保存编辑器区域参数
 * activeEffect: LayoutSetting.activeEffect, 选择active状态切换
 * init: LayoutSetting.init 初始化方法
 * }}
 */
var LayoutSetting = {
    rootStylesheet: null,
    targetElement: null,
    openFunc: function () {
        $('.pageHTML').on("click", ".layout-setting", function () {
            var oForm = $('#layout-setting');
            if ( $(this).attr('id') ){
                LayoutSetting.targetElement = $(this).parent();
                oForm.hide();
            } else {
                // 重写当前存储的layout的设置按钮jQuery对象
                LayoutSetting.targetElement = $(this).siblings('.view').children('.row');
                if(oForm.is(':hidden')) oForm.show();
            }

            // 设置编辑区参数回显
            SetStyleSheet.setConfig(LayoutSetting.targetElement);

            $('.mask-backdrop').show();
            var ele = $('#configuration');
            ele.show();
            var id = $(this).attr('data-target');
            $('.conf-body').find('.common-conf').each(function () {
                var oId = $(this).attr('data-id');
                if( oId === id) {
                    $('#data-btnGroup').hide();
                    $('#layout-btnGroup').show();
                    $(this).show();
                    cmdColorPicker();
                }
            });
            ele.stop().animate({
                right: 0
            }, 500);
        });
    },
    closeFunc: function () {
        $('#l-cancelBtn').click(function () {
            LayoutSetting.targetElement = null;
            editFunc.closeFunc();
            $('#layout-btnGroup').hide();
            $('#data-btnGroup').show();
        });
    },
    saveFunc: function () {
        $('#l-confBtn').click(function () {
            var ele = LayoutSetting.targetElement;
            GetStyleSheet.init(ele);
            LayoutSetting.targetElement = null;
            editFunc.closeFunc();
            $('#layout-btnGroup').hide();
            $('#data-btnGroup').show();
        });
    },
    activeEffect: function () {
        $('.js-bg-position').find('li').click(function () {
            $(this).toggleClass('active').siblings().removeClass('active');
        });
    },
    init: function () {
        LayoutSetting.openFunc();
        LayoutSetting.saveFunc();
        LayoutSetting.closeFunc();
        LayoutSetting.activeEffect();
        $('#layout-setting').validate();
    }
};
/**
 * 属性编辑器各个参数操作函数
 * @type {{
 * root: GetStyleSheet.root, 各种对象获取参数存储
 * bgColor: GetStyleSheet.bgColor, 获取背景颜色对象
 * bgImage: GetStyleSheet.bgImage, 获取背景图片对象
 * bgRepeat: GetStyleSheet.bgRepeat, 获取背景重复对象
 * bgSize: GetStyleSheet.bgSize, 获取背景尺寸对象
 * bgPosition: GetStyleSheet.bgPosition, 获取背景定位对象
 * distance: GetStyleSheet.distance, 获取定位参数
 * distanceData: GetStyleSheet.distanceData, 获取输入框数据
 * clearConfig: GetStyleSheet.clearConfig, 清理编辑区域
 * setStyle: GetStyleSheet.setStyle, 设置行内样式
 * init: GetStyleSheet.init 初始化方法
 * }}
 */
var GetStyleSheet = {
    root: function (ele) {
        var root = $('#layoutStyleSheet');
        var bgColor = root.find('.js-bg-color');
        var bgImg = root.find('.gallery-item-area-clone');
        var bgRepeat = root.find('.js-bg-repeat');
        var bgSize = root.find('.js-bg-size');
        var bgPosition = root.find('.js-bg-position');

        var distanceData = $('#layout-setting');

        var styleSheet = {};
        $.extend(styleSheet,
            GetStyleSheet.bgColor(bgColor),
            GetStyleSheet.bgImage(bgImg),
            GetStyleSheet.bgRepeat(bgRepeat),
            GetStyleSheet.bgSize(bgSize),
            GetStyleSheet.bgPosition(bgPosition),
            GetStyleSheet.distanceData(distanceData, 'padding'),
            GetStyleSheet.distanceData(distanceData, 'margin')
        );
        if (ele.attr('id') && distanceData.is(':visible')) {
            delete styleSheet['padding-top'];
            delete styleSheet['padding-bottom'];
            delete styleSheet['margin-top'];
            delete styleSheet['margin-bottom'];
        }
        if (ele.attr('id')) LayoutSetting.rootStylesheet = styleSheet;
        ele.attr('data-stylesheet', JSON.stringify(styleSheet));

        GetStyleSheet.setStyle(ele, styleSheet);
    },
    bgColor: function (ele) {
        if(ele.val())
            return {'background-color': ele.val() };
        else
            return {'background-color': 'transparent' };
    },
    bgImage: function (ele) {
        var bgImgae = {};
        if(ele.length == 1) {
            bgImgae['background-image'] = "url('"+ ele.find('img').attr('src') + "')";
            bgImgae['background-information'] = ele.attr('data-galleryinfo');
        }
        return bgImgae;
    },
    bgRepeat: function (ele) {
        if(ele.val() !== '0') return {'background-repeat': ele.val()};
    },
    bgSize: function (ele) {
        if(ele.val() !== '0') return {'background-size': ele.val()};
    },
    bgPosition: function (ele) {
        if(ele.find('.active').length) return {'background-position': ele.find('.active').attr('value')};
    },
    distance: function (val, distance) {
        switch(distance) {
            case 'paddingTop':
                if(+val) return {'padding-top': val + 'px'};
                break;
            case 'paddingBottom':
                if(+val) return {'padding-bottom': val + 'px'};
                break;
            case 'marginTop':
                if(+val) return {'margin-top': val + 'px'};
                break;
            case 'marginBottom':
                if(+val) return {'margin-bottom': val + 'px'};
                break;
        }
    },
    distanceData: function (ele, distance) {
        var distanceObj = {};
        var distanceTop, distanceBottom;
        if(distance == 'padding') {
            distanceTop = ele.find('input[name="paddingTop"]').val();
            distanceBottom = ele.find('input[name="paddingBottom"]').val();
        } else {
            distanceTop = ele.find('input[name="marginTop"]').val();
            distanceBottom = ele.find('input[name="marginBottom"]').val();
        }

        $.extend(distanceObj,
            GetStyleSheet.distance(distanceTop, distance + 'Top'),
            GetStyleSheet.distance(distanceBottom, distance + 'Bottom')
        );
        return distanceObj;
    },
    clearConfig: function () {
        var root = $('#layoutStyleSheet');
        var distanceData = $('#layout-setting');
        root.find('.js-bg-color').val('transparent');
        root.find('.js-bg-repeat').find('option').eq(0).prop('selected','selected');
        root.find('.js-bg-size').find('option').eq(0).prop('selected','selected');
        if (root.find('.js-bg-position').find('li.active').length > 0) {
            root.find('.js-bg-position').find('li.active').removeClass('active');
        }
        distanceData.find('input[name="paddingTop"]').val('');
        distanceData.find('input[name="paddingBottom"]').val('');
        distanceData.find('input[name="marginTop"]').val('');
        distanceData.find('input[name="marginBottom"]').val('');
    },
    removeStyle: function (ele) {
        ele.css({
            'background-color': '',
            'background-image': '',
            'background-repeat': '',
            'background-size': '',
            'background-position': ''
        });
    },
    setStyle: function (ele, data) {
        var e = ele.children('.column').length ? ele.children('.column') : ele;

        GetStyleSheet.removeStyle(e);

        $.each(data, function (k, v) {
            e.css(k,v);
        });
    },
    deleteBgImage: function () {
        $('#js-delete-bgImg').click(function () {
            var self = $('.gallery-content');
            var galleryItemArea = self.parent().siblings('.gallery-item-area');
            galleryItemArea.filter('.gallery-item-area-clone').remove();
            $(this).hide();
        });
    },
    init: function (ele) {
        GetStyleSheet.root(ele);
    }
};
GetStyleSheet.deleteBgImage();
/**
 * 编辑器参数回显函数
 * @type {{
 * setConfig: SetStyleSheet.setConfig, 获取当前自定义属性stylesheet的数据
 * setValue: SetStyleSheet.setValue 数据回显
 * }}
 */
var SetStyleSheet = {
    setConfig: function (ele) {
        var styleSheet = ele.attr('data-stylesheet');
        var DATA = styleSheet ? JSON.parse(styleSheet) : styleSheet;

        var self = $('.gallery-content');
        if (!self.attr('data-name')) return true;
        self.empty().append('<div><button class="js-addEditBtn btn btn-default" type="button">选择数据</button></div>');

        var galleryItemArea = self.parent().siblings('.gallery-item-area');
        galleryItemArea.filter('.gallery-item-area-clone').remove();

        if (!DATA) {
            GetStyleSheet.clearConfig();
        } else {
            SetStyleSheet.setValue(DATA)
        }
    },
    setValue: function (data) {

        var root = $('#layoutStyleSheet');
        var distanceData = $('#layout-setting');

        root.find('.js-bg-color').val(data['background-color']);
        if(data['background-repeat']) root.find('.js-bg-repeat').val(data['background-repeat']);
        if(data['background-size']) root.find('.js-bg-size').val(data['background-size']);
        root.find('.js-bg-position').find('li[value="'+ data['background-position'] + '"]').addClass('active');

        if(data['padding-top']) distanceData.find('input[name="paddingTop"]').val(data['padding-top'].replace('px',''));
        if(data['padding-bottom']) distanceData.find('input[name="paddingBottom"]').val(data['padding-bottom'].replace('px',''));
        if(data['margin-top']) distanceData.find('input[name="marginTop"]').val(data['margin-top'].replace('px',''));
        if(data['margin-bottom']) distanceData.find('input[name="marginBottom"]').val(data['margin-bottom'].replace('px',''));

        SetStyleSheet.setImage(data['background-image'], data['background-information']);
    },
    setImage: function () {
        var btn = $('#js-delete-bgImg');
        btn.hide();
        if (arguments[0]) {
            var self = $('.gallery-content');
            var galleryItemArea = self.parent().siblings('.gallery-item-area');
            var newArea = galleryItemArea.clone();

            var img = arguments[0].match(/\'([^\']*)\'/)[1];
            var info = arguments[1].split(',');
            newArea.addClass('gallery-item-area-clone');
            newArea.attr('galleryItemSerial', info[0]);
            newArea.attr('data-galleryInfo', arguments[1]);

            // 寻找里面的元素
            var replacer = function (text) {
                if (!text)
                    return text;
                text = text.replace(/!\{title}/g, info[1]);
                text = text.replace(/!\{serial}/g, info[2]);
                text = text.replace(/!\{src}/g, img);
                text = text.replace(/!\{url}/g, img);
                return text;
            };

            newArea.contents().each(function (index, ele) {
                var eleJ = $(ele);

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
            newArea.insertBefore(galleryItemArea);
            
            if (newArea.length)  btn.show();
        }
    }
};

function galleryRender (data) {
    var self = $(this);
    if (!self.attr('data-name')) return true;

    var galleryItemArea = self.parent().siblings('.gallery-item-area');
    galleryItemArea.filter('.gallery-item-area-clone').remove();

    if (galleryItemArea.size() > 0) {
        galleryItemArea.each(function (index, area) {

            if(data) {
                var newArea = $(area).clone();
                var item = data[0];
                // 身份资别
                newArea.addClass('gallery-item-area-clone');
                newArea.attr('galleryItemSerial', item.uuid);
                newArea.attr('data-galleryInfo', item.uuid+','+item.name+','+item.serial);
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

                if (newArea.length) $('#js-delete-bgImg').show();
            }
        });

    }
}
LayoutSetting.init();
