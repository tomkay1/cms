/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

/**
 * 增加了几个function用于消息提示
 *
 * Created by CJ on 6/26/16.
 */

function showPrimary(title, text, image, time) {
    jQuery.gritter.add({
        title: title,
        text: text,
        class_name: 'growl-primary',
        image: image,
        sticky: false,
        time: time ? time : ''
    });
}
function showSuccess(title, text, image, time) {
    jQuery.gritter.add({
        title: title,
        text: text,
        class_name: 'growl-success',
        image: image,
        sticky: false,
        time: time ? time : ''
    });
}
function showWarning(title, text, image, time) {
    jQuery.gritter.add({
        title: title,
        text: text,
        class_name: 'growl-warning',
        image: image,
        sticky: false,
        time: time ? time : ''
    });
}
function showDanger(title, text, image, time) {
    jQuery.gritter.add({
        title: title,
        text: text,
        class_name: 'growl-danger',
        image: image,
        sticky: false,
        time: time ? time : ''
    });
}
function showInfo(title, text, image, time) {
    jQuery.gritter.add({
        title: title,
        text: text,
        class_name: 'growl-info',
        image: image,
        sticky: false,
        time: time ? time : ''
    });
}