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
var GlobalID;

function widgetProperties( id ) {
    return wsCache.get(id) || {};
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
    createStore: function (ele) {
        GlobalID = $(ele).siblings('.view').children().attr('id');
        widgetHandle.setStroe(GlobalID);
        widgetHandle.initFunc(GlobalID);
    },
    setStroe: function (id, data) {
        if ( data ) {
            wsCache.set(id, { 'properties' : data });
        } else {
            wsCache.set(id, { 'properties' : {} });
        }
    },
    getGlobalFunc: function (id) {
        if ( !id ) return false;
        var arr = id.split('-');
        return window[arr[0]];
    },
    initFunc: function (id) {
        var fn = widgetHandle.getGlobalFunc(id);
        if( fn && typeof fn.init === 'function' ) {
            fn.init(id);
        }
    },
    saveFunc: function (id) {
        var fn = widgetHandle.getGlobalFunc(id);
        var path = '/preview/PageID/'+id+'.css';
        dynamicLoading.css(path);
        if( fn && typeof fn.saveCompoent === 'function' ) {
            var properties = fn.saveCompoent();
            if( properties !== null && !$.isEmptyObject(properties)) {
                widgetHandle.setStroe(GlobalID, properties);
                updataCompoentPreview(id, properties);
                editFunc.closeFunc();
            }
        }
    }
};

function updataCompoentPreview(globalID, properties) {
    var ele = $('#' + globalID);
    var widgetId = ele.data('widgetidentity');
    var styleId = ele.data('styleid');
    $.ajax({
        type: 'POST',
        url: '/previewHTML',
        dataType: 'html',
        data: {
            widgetidentity: widgetId,
            styleId: styleId,
            properties: properties
        },
        success: function (json) {
            if (json.statusCode == '200') {
                ele.html(json.body);
                editFunc.closeFunc();
                layer.msg('操作成功', {time: 2000});
            }
            if (json.statusCode == '403') {
                layer.msg('没有权限', {time: 2000});
            }
            if (json.statusCode == '502') {
                ayer.msg('服务器错误,请稍后再试', {time: 2000});
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(errorThrown);
            layer.msg('服务器错误,请稍后再试', {time: 2000});
        }
    });
}

function getDataSource(url, parentID) {
    var dataSource = null;
    $.ajax({
        type: 'POST',
        url: url,
        dataType: 'json',
        data: {
            parentID: parentID
        },
        success: function (json) {
            if (json.statusCode == '200') {
                dataSource = json.body;
                return dataSource;
            }
            if (json.statusCode == '204') {
                layer.msg('没有找到数据', {time: 2000});
                return dataSource;
            }
            if (json.statusCode == '502') {
                layer.msg('服务器错误,请稍后再试', {time: 2000});
                return dataSource;
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(errorThrown);
            layer.msg('服务器错误,请稍后再试', {time: 2000});
            return dataSource;
        }
    });
}
/**
* 动态加载组件的 JS文件
* @type {{css: dynamicLoading.css, js: dynamicLoading.js}}
*/
var dynamicLoading = {
    css: function(path){
        if( !path || path.length === 0){
            console.error('参数 "path" 是必需的！');
        }
        var exist = false;
        var ele = $('link');
        $.each(ele, function (i, v) {
            var href = $(v).attr('href');
            if ( href.indexOf(path) != -1 ) {
                exist = true;
                $(v).attr('href', path + '?t=' + +new Date());
            }
        });
        if (!exist) {
            var lastElement = ele.last();
            var link = $('<link>');
            link.attr({'rel': 'stylesheet', 'href': path + '?t=' + +new Date()});
            lastElement.after(link);
        }
    },
    js: function(path){
        if( !path || path.length === 0){
            console.error('参数 "path" 是必需的！');
        }
        var lastElement = $('script').last();
        var script = $('<script></script>');
        script.attr('src', path);
        lastElement.after(script);
    }
};

/**
 * 所有参数都有初始化默认
 * @param obj [] 参数对象
 * @param obj.ui [String] 绑定元素的 class 或者 id 如：#test
 * @param obj.inputName [String] imput[type=file] name值，传给接口的参数名
 * @param obj.maxWidth [Number] 图片宽度规格
 * @param obj.maxHeight [Number] 图片高度
 * @param obj.maxFileCount [Number] 限制上传的图片数量，不限制参数为 -1
 * @param obj.uploadUrl [String] 上传图片接口地址
 * @param obj.successCallback [Function] 上传成功后回调函数
 * @param obj.deleteUrl [String] 删除图片接口地址. 为空就与 uploadUrl同一值
 * @param obj.deleteCallback [Function] 删除成功后回调函数
 * @param obj.isCongruent [Boolean] 是否启用完全相等
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
        multiple:false,
        dragDrop:false,
        maxFileCount: maxFileCount,
        abortStr:"中止",
        cancelStr: "取消",
        deletelStr:"删除",
        uploadStr:"上传图片",
        maxFileCountErrorStr:" 不可以上传. 最大数量: ",
        showPreview:true,
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
        },
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