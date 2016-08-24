/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

$(function () {

    // uploader
    $.cmsUploader($('#article-uploader, #link-uploader, #video-uploader, #gallery-uploader'), function (path) {
        $("#thumbUri").val(path);
    }, {
        allowedExtensions: ['jpeg', 'jpg', 'png', 'bmp'],
        itemLimit: 1,
        sizeLimit: 3 * 1024 * 1024
    });

    // common
    var categories = $('option', $('#categories'));

    var parentCategoryId = $('select[name=parentCategoryId]');
    var parentOptions = $('option', parentCategoryId);

    var categoryName = $('input[name=categoryName]');
    categoryName.change(function () {

        // 找到那个option
        var val = $(this).val();
        // 我们找下是否在列表内

        parentOptions.attr('disabled', false);

        var targetOption = categories.filter(function (index, ele) {
            return $(ele).val() == val;
        }).first();

        if (targetOption.size() > 0) {
            // 的确是选择的
            // parentCategoryId.prop('disabled', 'disabled');
            var parentId = targetOption.attr('parentId');
            if (parentId && parentId.length > 0) {
                //有父类的
                parentCategoryId.val(parentId);
            } else {
                //置空
                parentCategoryId.val('');
            }
            $('option:not(:selected)', parentCategoryId).attr('disabled', true);
        } else {
            // 手工录入的 保留原值么?
            // parentCategoryId.prop('disabled', '');
        }
        // console.log('changed', val, categories, targetOption);
    });

    var rules = {
        categoryName: {
            required: true
        }, title: {
            required: true,
            maxlength: 48
        }
    };
    var messages = {
        categoryName: {
            required: "必须选择或者输入一个数据源名称"
        }, title: {
            required: "需要标题",
            maxlength: "标题长度太长了"
        }
    };

    // form
    // 文章
    $('#articleForm').validate({
        rules: jQuery.extend(true, {
            content: {
                required: true
            }
        }, rules),
        messages: jQuery.extend(true, {
            content: {
                required: "请输入文章的正文"
            }
        }, messages)
    });

    // 链接
    $('#linkForm').validate({
        rules: jQuery.extend(true, {
            linkUrl: {
                required: true,
                url: true
            }
        }, rules),
        messages: jQuery.extend(true, {
            linkUrl: {
                required: "请输入绝对的链接",
                url: '请输入有效的URL链接'
            }
        }, messages)
    });

    // 公告
    $('#noticeForm').validate({
        rules: jQuery.extend(true, {
            content: {
                required: true
            }
        }, rules),
        messages: jQuery.extend(true, {
            content: {
                required: "请输入公告的正文"
            }
        }, messages)
    });

    // 下载
    var downloadForm = $('#downloadForm');
    downloadForm.on('submit', function () {
        // <input type="hidden" name="tempPath" id="downloadUrl">
        // <label for="title" class="error" style="display: inline-block;">需要标题</label>
        // console.log('submit me? ', this);
        // 如果当前form并不是新增 那么就无需要求上传
        var action = $(this).attr('action');
        var index = action.lastIndexOf('/');
        if (index > 0) {
            var isNumber = action.substring(index + 1, action.length);
            try {
                if (parseInt(isNumber) > 0)
                    return true;
            } catch (e) {
                //继续
            }
        }

        var tempPath = $('input[name=tempPath]', $(this));
        if (!tempPath.val() || tempPath.val().length < 0) {
            var label = $('span[class=error]', tempPath.parent());
            if (label.size() > 0) {
                label.css('display', 'block');
            } else {
                tempPath.parent().append('<span class="error" style="display: block;margin-left: 160px;color: #B94A48;">需要上传资源</span>');
            }
            return false;
        }
        return true;
    });


    $.cmsUploader($('#download-uploader'), function (path) {
        $("#downloadUrl").val(path);
        var tempPath = $('input[name=tempPath]', downloadForm);
        var label = $('span[class=error]', tempPath.parent());
        if (label.size() > 0) {
            label.css('display', 'none');
        }
    }, {
        itemLimit: 1
    });

    downloadForm.validate({
        rules: jQuery.extend(true, {
            fileName: {
                required: true,
                maxlength: 48
            }
        }, rules),
        messages: jQuery.extend(true, {
            fileName: {
                required: "请输入文件名",
                maxlength: "文件名长度过长"
            }
        }, messages)
    });

    // 图库
    var session = null;
    if ($.galleryItemsUrl && $.galleryItemsUrl.length > 0) {
        session = {
            endpoint: $.galleryItemsUrl
        }
    }
    $.cmsUploader($('#gallery-item-uploader'), function (path) {
        var tempImagePaths = $('input[name=tempImagePaths]');
        var val = tempImagePaths.val();
        if (!val) val = '';
        if (val.length > 0)
            val = val + ',';
        val = val + path;
        tempImagePaths.val(val);
    }, {
        allowedExtensions: ['jpeg', 'jpg', 'png', 'bmp'],
        itemLimit: 25,
        sizeLimit: 3 * 1024 * 1024
    }, session);

    $('#galleryForm').validate({
        rules: jQuery.extend(true, {}, rules),
        messages: jQuery.extend(true, {}, messages)
    });

});

