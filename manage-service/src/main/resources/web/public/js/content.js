/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

/**
 * 关于文章的脚本
 * Created by Neo on 7/7/16.
 */

//$('#articleDate').mask('9999-99-99');


function logoUploaded(id, name, responseJSON) {
    if(responseJSON.success){
        $("#thumbUri").val(responseJSON.newUuid);
    }
    console.log(responseJSON.newUuid);
    // newUuid is the path
}

function logoOnUpload() {
    //maybe in protype
    console.log('logoOnUpload');
    console.log.apply(console, arguments);
}

//noinspection JSUnresolvedFunction
$('#article-uploader, #link-uploader, #video-uploader, #gallery-uploader').fineUploader({
    debug:true,
    template: top.$('#qq-template').get(0),
    request: {
        inputName: 'file',
        // endpoint: 'http://mycms.51flashmall.com:8080/manage/upload/fine'
        endpoint: uploadFileUrl
    },
    thumbnails: {
        placeholders: {
            waitingPath: 'http://resali.huobanplus.com/cdn/jquery-fine-uploader/5.10.0/placeholders/waiting-generic.png',
            notAvailablePath: 'http://resali.huobanplus.com/cdn/jquery-fine-uploader/5.10.0/placeholders/not_available-generic.png'
        }
    },
    validation: {
        allowedExtensions: ['jpeg', 'jpg', 'png', 'bmp'],
        itemLimit: 1,
        sizeLimit: 3 * 1024 * 1024
    },
    callbacks:{
        onComplete: function (id, name, response) {
            console.log(response);
            if(response.success){
                if($("#thumbUri").size()>0)
                    $("#thumbUri").val(response.newUuid);
                else if($("#videoUrl").size()>0)
                    $("#videoUrl").val(response.newUuid);
            }
        },
        onError: logoOnUpload,
        onSubmit: logoOnUpload,
        onCancel: logoOnUpload,
        onValidate: logoOnUpload
    }
});

$('#download-uploader').fineUploader({
    debug:true,
    template: top.$('#qq-template').get(0),
    request: {
        inputName: 'file',
        // endpoint: 'http://mycms.51flashmall.com:8080/manage/upload/fine'
        endpoint: uploadFileUrl
    },
    thumbnails: {
        placeholders: {
            waitingPath: 'http://resali.huobanplus.com/cdn/jquery-fine-uploader/5.10.0/placeholders/waiting-generic.png',
            notAvailablePath: 'http://resali.huobanplus.com/cdn/jquery-fine-uploader/5.10.0/placeholders/not_available-generic.png'
        }
    },
    callbacks:{
        onComplete: function (id, name, response) {
            console.log(response);
            if(response.success){
                $("#downloadUrl").val(response.newUuid);
            }
        },
        onError: logoOnUpload,
        onSubmit: logoOnUpload,
        onCancel: logoOnUpload,
        onValidate: logoOnUpload
    }
});