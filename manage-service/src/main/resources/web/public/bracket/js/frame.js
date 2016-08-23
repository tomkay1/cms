/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

/**
 * 菜单JS,用于iframe子页面调用
 * 可以将选择的菜单点亮而关闭其他菜单
 * 要求子页面将body的id定义为指定菜单的class
 * Created by CJ on 6/25/16.
 */

$(function () {
    var debug = false;

    var print = function () {
        if (debug)
            console.log.apply(console, arguments);
    };

    function resetTopMenuStatus() {
        var top = window.top;
        if (!top) {
            console.error('需要在iframe中调用');
            return;
        }
        if (!top.$) {
            console.error('所在frame页面并不支持jQuery');
            return;
        }

        var menuUl = top.$('.nav').not('.mb30').not('.nav-justified');
        print('found menu:', menuUl);
        // 先移除
        $('ul', $('.nav-active', menuUl)).css('display', 'none');
        $('.nav-active', menuUl).removeClass('nav-active');
        $('.active', menuUl).removeClass('active');
        print('done to remove');

        var targetClass = document.body.id;
        print('targetClass:', targetClass);

        // 寻找目标
        var target = top.$('.' + targetClass, menuUl);
        print('target:', target);

        if (target.size() == 0) {
            console.error('找不到对应的Class', targetClass);
            return;
        }
        //先确定下它的上级菜单
        var parent = target.closest('.nav-parent');
        print('parent:', parent);

        target.closest('li').addClass('active');
        target.closest('.nav-parent').addClass('nav-active');

        $('ul', target.closest('.nav-parent')).css('display', 'block');
    }

    resetTopMenuStatus();

    // 点取消 你给我提交! 你tmd逗我啊
    $('button.btn-default').click(function () {
        history.back();
        return false;
    });

    // 让delete class 具备确认能力
    function followTheLink() {
        var link = $(this).attr('href');
        print('link button link:', link);
        location.href = link;
    }

    var linkButtons = $('.link-button');

    linkButtons.not('.delete').click(function () {
        followTheLink.call(this);
    });

    $('.delete').not('.link-button').click(function () {
        return confirm('确实要删除么?');
    });

    linkButtons.filter('.delete').click(function () {
        if (confirm('确实要删除么?')) {
            followTheLink.call(this);
        }
    });

    // 让.datatable 变成真的datatable
    // table
    var dataTable = $('table.table')
    if (dataTable.size() > 0) {
        dataTable.dataTable({
            language: {
                url: 'http://resali.huobanplus.com/cdn/bracket/localisation/dataTable_zh_CN.json'
            },
            sPaginationType: "full_numbers"
        });
    }

    // 让autoGrow class 可以自动增长
    var autoGrow = $('.autoGrow');
    if (autoGrow.size() > 0) {
        //先判断长度是为了避免每个页面都需要载入autogrow页面
        autoGrow.autogrow();
    }
    // 让inputTags class 可以变成点击添加
    var inputTags = $('.inputTags');
    inputTags.each(function (index, ele) {
        var input = $(ele);
        var config = {width: 'auto'};
        var tagsDefaultText = input.attr('tagsDefaultText');
        var tagsWidth = input.attr('tagsWidth');

        if (tagsDefaultText) {
            config.defaultText = tagsDefaultText;
        }
        if (tagsWidth) {
            config.width = tagsWidth;
        }
        input.tagsInput(config);
    });


    // date picker ?
    var datepicker = $('.cms-datepicker');
    if (datepicker.size() > 0)
        datepicker.datepicker();

    // ck editor
    var editor = $(".editor");
    if (editor.size() > 0) {
        var url = top.$.imageUploaderUrl;
        url = url || 'http://cms.51flashmall.com/manage/upload/image';
        editor.ckeditor({
            extraPlugins: 'uploadimage',
            uploadUrl: url,
            language: top.$.language
        });
    }

    // 让chosen-select可以自动处理
    var chosenSelect = $('.chosen-select');
    if (chosenSelect.size() > 0)
        chosenSelect.chosen({'width': '100%', 'white-space': 'nowrap'});

    // 文件上传者
    /**
     * 使用该方法需要在iframe页面中 初始化$.cmsUploaderUrl 如果是原型环境该url需为null
     * 同时也需要存在id为qq-template的上传模板
     * @param ui 需要变成uploader的jquery集合
     * @param uploadedPathConsumer CMS的uploader会在响应中给予path,这就是上传以后的path;原型环境也会杜撰一个path执行
     * @param validation 是否允许该文件的的校验 http://docs.fineuploader.com/branch/master/api/options.html#validation
     */
    $.cmsUploader = function (ui, uploadedPathConsumer, validation) {
        if (ui.size() == 0)
            return;
        var request;
        if (!top.$.cmsUploaderUrl) {
            request = {};
        } else {
            request = {
                inputName: 'file',
                endpoint: top.$.cmsUploaderUrl
            };
        }
        ui.fineUploader({
            template: top.$('#qq-template').get(0),
            request: request,
            thumbnails: {
                placeholders: {
                    waitingPath: 'http://resali.huobanplus.com/cdn/jquery-fine-uploader/5.10.0/placeholders/waiting-generic.png',
                    notAvailablePath: 'http://resali.huobanplus.com/cdn/jquery-fine-uploader/5.10.0/placeholders/not_available-generic.png'
                }
            },
            validation: validation,
            callbacks: {
                onComplete: function (id, name, responseJSON) {
                    print('onComplete', id, name, responseJSON);
                    if (responseJSON.newUuid)
                        uploadedPathConsumer(responseJSON.newUuid);
                },
                onValidate: function (data, buttonContainer) {
                    print('onValidate', top.$.cmsUploaderUrl, data);
                    if (!top.$.cmsUploaderUrl) { //原型
                        uploadedPathConsumer(data.name);
                    }
                }
            }
        });
    }
});
