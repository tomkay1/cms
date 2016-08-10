/**
 * Created by Neo on 2016/7/21.
 */

/* 使用 sessionStorage 存储操作数据*/
var wsCache = new WebStorageCache({storage: 'sessionStorage'});
/* 判断是支持本地存储，如果不能无法完成后续操作*/
if (!wsCache.isSupported()) {
    layer.alert('您的浏览器无法支持页面功能，推荐使用360极速浏览器，再页面进行操作。');
}

/**
 * GlobalID 全局参数，用于存储当前操作组件的唯一 ID
 */
var GlobalID, identity;

/**
 *  初始化 wsCache.get(id) 若为 null 组件初始化状态，该组件的 properties 为对应控件的默认 properties
 *  若不为 null 组件再次操作，返回该组件的 properties，可为空
 * @param id 传入GlobalID;
 * @returns 返回对应properties
 */
function widgetProperties( id ) {
    var ele = $('#' + id);
    var identity = ele.data('widgetidentity');
    var dataCache = wsCache.get(id);
    if ( dataCache ) {
        return dataCache.properties;
    } else {
        return wsCache.get(identity).properties;
    }

};

/**
 * 组件数据控制逻辑
 * createStore: 初始化GlobalID
 * setStroe: 设置操作组件的 properties 本地数据存储
 * getGlobalFunc: 获取GlobalID
 * initFunc: 动态调用组件初始化方法
 * saveFunc：动态调用组件保存方法
 */
var widgetHandle = {
    getIdentity: function (ele, callback) {
        identity = $(ele).siblings('.view').children().data('widgetidentity');
        callback&&callback(identity);
    },
    createStore: function (ele) {
        GlobalID = $(ele).siblings('.view').children().attr('id');
        var data = widgetProperties(GlobalID);
        if (wsCache.get(GlobalID) == null) widgetHandle.setStroe(GlobalID, data);
        widgetHandle.getIdentity(ele ,function (identity) {
            dynamicLoading.js( wsCache.get(identity).script);
            if ( CMSWidgets )  CMSWidgets.openEditor(GlobalID,identity);
        });
    },
    setStroe: function (id, data) {
        if ( data ) {
            wsCache.set(id, { 'properties' : data });
        } else {
            wsCache.set(id, { 'properties' : {} });
        }
    },
    saveFunc: function (id) {
        CMSWidgets.saveComponent(id, {
            onSuccess: function (ps) {
                if ( ps !== null) {
                    widgetHandle.setStroe(id, ps);
                    updataCompoentPreview(id, ps);
                }
                editFunc.closeFunc();
            },
            onFailed: function (msg) {
                layer.msg(msg)
            }
        });
        CMSWidgets.closeEditor(GlobalID,identity);
    },
    closeSetting: function () {
        CMSWidgets.closeEditor(GlobalID,identity);
        editFunc.closeFunc();
    }
};
function updataCompoentPreview(globalID, properties) {
    var ele = $('#' + globalID);
    var widgetId = ele.data('widgetidentity');
    var styleId = ele.data('styleid');
    var data = {
        "widgetIdentity": widgetId,
        "styleId": styleId,
        "properties": properties,
        "pageId": pageId,
        "componentId": globalID
    };
    var loading = layer.load(2);
    $.ajax({
        type: 'POST',
        url: '/preview/component',
        dataType: 'html',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data),
        statusCode: {
            403: function() {
                layer.msg('没有权限', {time: 2000});
                editFunc.closePreloader();
            },
            404: function() {
                layer.msg('服务器请求失败', {time: 2000});
                editFunc.closePreloader();
            },
            502: function () {
                layer.msg('服务器错误,请稍后再试', {time: 2000});
                editFunc.closePreloader();
            }
        },
        success: function (html, textStatus, jqXHR) {
            if (html) {
                var updateHtml = $(html);
                ele.html(updateHtml.html());
                editFunc.closeFunc();
                layer.close(loading);
                layer.msg('操作成功', {time: 2000});
                var path = jqXHR.getResponseHeader('cssLocation');
                if (path) dynamicLoading.css(path);
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(errorThrown);
            layer.close(loading);
            layer.msg('服务器错误,请稍后再试', {time: 2000});
        }
    });
}

/**
 *
 * @param type 表示查询的数据类别 比如 findGalleryItem
 * @param parameter 查询参数 可选
 *
 * @param onSuccess 成功回调 参考 http://api.jquery.com/jquery.ajax/#success
 * @param onError 错误回调 参考 http://api.jquery.com/jquery.ajax/#error
 */
function getDataSource(type, parameter, onSuccess, onError) {
    if (_CMS_DataSource_URI == null) {
        console.log('invoke getDataSource in po..');
        // TODO 根据不同的type给予不同的mock数据
        onSuccess({});
        return;
    }
    var url = _CMS_DataSource_URI + type;
    if (parameter != null) {
        url = url + "/" + parameter;
    }
    console.error("url:"+url);
    $.ajax({
        type: 'GET',
        url: url,
        dataType: 'json',
        async: !testMode,
        success: onSuccess,
        error: onError
    });
}
/**
* 动态加载组件的 JS文件
* @type {{css: dynamicLoading.css, js: dynamicLoading.js}}
*/
var dynamicLoading = {
    init: function (type, path) {
        if( !path || path.length === 0){
            console.error('参数 "path" 是必需的！');
        }
        var exist = false;
        var handler = (type === 'css');
        var ele =  handler ? $('link') : $('script');
        $.each(ele, function (i, v) {
            var attr = handler ? 'href' : 'src';
            var argv = $(v).attr(attr);
            if ( argv && argv.indexOf(path) != -1 ) {
                exist = true;
                $(v).attr(attr, path);
            }
        });
        if (!exist) {
            var lastElement = ele.last();
            var addEle = handler ? $('<link>') : $('<script></script>');
            if ( handler ) {
                addEle.attr({'rel': 'stylesheet', 'href': path });
            } else {
                addEle.attr('src', path);
            }
            lastElement.after(addEle);
        }
    },
    css: function(path){
        dynamicLoading.init('css', path);
    },
    js: function(path){
        dynamicLoading.init('js', path);
    }
};

/**
 * 所有参数都有初始化默认
 * @param obj [] 参数对象
 * @param obj.ui [String] 绑定元素的 class 或者 id 如：#test
 * @param obj.inputName [String] imput[type=file] name值，传给接口的参数名 默认 file
 * @param obj.maxWidth [Number] 图片宽度规格 默认 1920
 * @param obj.maxHeight [Number] 图片高度 默认 1080
 * @param obj.maxFileCount [Number] 限制上传的图片数量，-1 不限制参数为 默认 -1
 * @param obj.uploadUrl [String] 上传图片接口地址 默认 /manage/cms/resourceUpload
 * @param obj.successCallback [Function] 上传成功后回调函数 默认为空
 * @param obj.deleteUrl [String] 删除图片接口地址. 默认 /manage/cms/deleteResource
 * @param obj.deleteCallback [Function] 删除成功后回调函数 默认为空
 * @param obj.isCongruent [Boolean] 是否启用完全相等，false 不启用，默认 false
 */
function uploadForm (obj) {
    var ui = obj.ui,
        inputName = obj.inputName || 'file',
        maxWidth = obj.maxWidth || 1920,
        maxHeight = obj.maxHeight || 1080,
        maxFileCount = obj.maxFileCount || -1,
        uploadUrl = obj.uploadUrl || '/manage/cms/resourceUpload',
        successCallback = obj.successCallback || function () {},
        deleteUrl = obj.deleteUrl || '/manage/cms/deleteResource',
        deleteCallback = obj.deleteCallback || function () {},
        sign = obj.isCongruent || false;

    var uploadFile = $(ui).uploadFile({
        url: uploadUrl,
        showFileCounter: false,
        returnType: "json",
        fileName: inputName,
        multiple:true,
        maxFileCount: maxFileCount,
        dragDropStr:"<span>拖拽至此</span>",
        abortStr:"中止",
        cancelStr: "取消",
        deletelStr:"删除",
        uploadStr:"上传图片",
        maxFileCountErrorStr:" 不可以上传. 最大数量: ",
        showPreview:true,
        statusBarWidth:360,
        previewHeight: "60px",
        previewWidth: "60px",
        showDelete: true,
        autoSubmit: false,
        onSuccess: successCallback,
        onSelect:function(files) {

            var file = files[0];
            var reader = new FileReader();
            reader.onload = function(e) {
                var data = e.target.result;
                var image = new Image();
                image.src = data;
                image.onload = function(){
                    var width = image.width;
                    var height = image.height;
                    var vWidth = sign ? width == maxWidth : width >= maxWidth;
                    var vHeight = sign ? height == maxHeight : height >= maxHeight;
                    verifySize(sign, vWidth, vHeight, function () {
                        uploadFile.startUpload()
                    });
                };

            };
            reader.readAsDataURL(file);

            return true;
        },
        onError: function (files, status, message, pd) {
            pd.statusbar.hide();
            layer.msg('上传失败，请稍后再说');
        },
        deleteCallback: function (data, pd) {
            for (var i = 0; i < data.length; i++) {
                $.post(deleteUrl, { op: "delete", name: data[i] }, deleteCallback);
            }
            pd.statusbar.hide();
        }
    });
};


/**
 *
 * @param congruent [Boolean] 是否启用完全相等
 * @param vWidth [Boolean]  宽度验证返回值
 * @param vHeight [Boolean] 高度验证返回值
 * @param callback [Function] 验证成功后的回调函数
 */
function verifySize(congruent, vWidth, vHeight, callback) {
    var widthText = congruent ? '图片宽度不符合限制' : '图片宽度超出限制';
    var heightText = congruent ? '图片高度不符合限制' : '图片高度超出限制';

    if (congruent) {
        if (!vWidth) layer.msg(widthText);
        if (!vHeight) layer.msg(heightText);
        if ( vWidth === true && vHeight === true ) callback();
    } else {
        if (vWidth) layer.msg(widthText);
        if (vHeight) layer.msg(heightText);
        if ( !vWidth === true && !vHeight === true ) callback();
    }
};


