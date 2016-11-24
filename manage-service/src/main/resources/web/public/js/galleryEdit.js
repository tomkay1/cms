/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

/**
 * 图库编辑专用
 * Created by CJ on 8/24/16.
 */

var manualUploader = new qq.FineUploader({
    element: document.getElementById('gallery-items-uploader'),
    template: 'qq-template-manual-trigger',
    request: {
        endpoint: $.galleryItemsUrl,
        method: top.$.prototypesMode ? 'GET' : 'POST'
    },
    thumbnails: {
        placeholders: {
            waitingPath: 'http://resali.huobanplus.com/cdn/jquery-fine-uploader/5.10.0/placeholders/waiting-generic.png',
            notAvailablePath: 'http://resali.huobanplus.com/cdn/jquery-fine-uploader/5.10.0/placeholders/not_available-generic.png'
        }
    },
    validation: {
        allowedExtensions: ['jpeg', 'jpg', 'png', 'bmp'],
        itemLimit: 25,
        sizeLimit: 3 * 1024 * 1024
    },
    session: {
        endpoint: $.galleryItemsUrl
    },
    deleteFile: {
        enabled: true,
        endpoint: $.galleryItemsUrl
    },
    autoUpload: false,
    debug: top.$.testMode
});

qq(document.getElementById("trigger-upload")).attach("click", function () {
    manualUploader.uploadStoredFiles();
});


