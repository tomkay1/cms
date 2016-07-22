/**
 * Created by Neo on 2016/7/21.
 */
/**
 * GlobalID 全局参数，用于存储当前操作组件的唯一 ID
 */
var GlobalID;
function createStore(ele) {
    GlobalID= $(ele).siblings('.view').children().attr('id');
    store.set(GlobalID, { properties : {} });
};

function widgetProperties( id ) {
    console.log(id);
    return store.get(id) || {};
};

/**
* 动态加载组件的 JS文件
* @type {{css: dynamicLoading.css, js: dynamicLoading.js}}
*/
var dynamicLoading = {
    css: function(path){
        if( !path || path.length === 0){
            console.error('参数 "path" 是必需的！');
        }
        // Todo
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
 *
 * @param obj [] 参数对象
 * @param obj.ui [String] 绑定元素的 class 或者 id 如：#test
 * @param obj.inputName [String] imput[type=file] name值，传给接口的参数名
 * @param obj.maxWidth [Number] 图片宽度规格
 * @param obj.maxHeight [Number] 图片高度
 * @param obj.maxFileCount [Number] 限制上传的图片数量，不限制参数为 -1
 * @param obj.uploadUrl [String] 上传图片接口地址
 * @param obj.successCallback [Function] 上传成功后回调函数
 * @param obj.deleteUrl [String] 删除图片接口地址
 * @param obj.deleteCallback [Function] 删除成功后回调函数
 * @param obj.isCongruent [Boolean] 是否启用完全相等
 */
function uploadForm (obj) {
    var ui = obj.ui,
        inputName = obj.inputName || 'file',
        maxWidth = obj.maxWidth || 9999,
        maxHeight = obj.maxHeight || 9999,
        maxFileCount = obj.maxFileCount || -1,
        uploadUrl = obj.uploadUrl,
        successCallback = obj.successCallback || function () {},
        deleteUrl = obj.deleteUrl,
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