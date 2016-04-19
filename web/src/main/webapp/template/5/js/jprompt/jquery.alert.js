$.extend($, {

    /*
   *Description:发送短信60秒倒计时
   *Author:xhl
   *Time:2015/06/23
   */
    LastTime: function (el, disCls) {
        if (el.length < 1)
            return;
        var oldText = el.html();
        var functionName = el.attr("onclick");
        el.removeAttr("onclick", "");
        if (el.hasClass(disCls)) {
            el.addClass("disabled");
            el.removeClass(disCls);
            var time = 60;
            var elTime = el.find("span");
            if (elTime.length == 0) {
                el.html("<span>" + time + "</span>秒");
                elTime = el.find("span");
            }
            var timer = setInterval(function () {
                if (time > 1) {
                    var str = time - 1;
                    time = time - 1;
                    elTime.html(str);
                } else {
                    str = "";
                    el.html(oldText);
                    el.addClass(disCls);
                    el.removeClass("disabled");
                    if (functionName) { el.attr("onclick", functionName); }
                    clearInterval(timer);
                }

            }, 1000);
        } else {
            el.addClass(disCls);
            var time = 60;
            var elTime = el.find("span");
            if (elTime.length == 0) {
                el.html("<span>" + time + "</span>秒");
                elTime = el.find("span");
            }
            var timer = setInterval(function () {
                if (time > 1) {
                    var str = time - 1;
                    time = time - 1;
                    elTime.html(str);
                } else {
                    str = "";
                    el.html(oldText);
                    if (functionName) { el.attr("onclick", functionName); }
                    el.removeClass(disCls);
                    clearInterval(timer);
                }

            }, 1000)
        }
    },
    /*
    *Description:重构Alert特效
    *Author:xhl
    *Time:2015/06/24
    */
    setAlert: function (message, position, delay) {//自动消失弹窗  a:提示语;（已重写成功）
        var $w = $(window), $toast = $('<div class="ui-popup disappearDialog">' + message + '</div>');
        $('.disappearDialog').remove();
        var top = '50%', left = "50%", m = "0 0 0 0";
        if (!isNaN(position)) {
            delay = Math.max(1000, Number(position));
        }
        else {
            delay = 2000;
        }
        var removeToast = function () {
            $(this).remove();
        };
        $toast.appendTo($(document.body)).delay(delay);
        if (position == 'top') {
            top = 75;
        }
        else if (position == 'bottom') {
            top = $w.height() - $toast.outerHeight() / 2 - 70;
        }
        if ($w.width() == $toast.outerWidth(true)) {
            left = 0;
            m = '-' + $toast.outerHeight() / 2 + 'px 1rem 0 1rem';
        } else {
            m = '-' + $toast.outerHeight() / 2 + 'px 0 0 -' + $toast.outerWidth() / 2 + 'px';
        }
        $toast.css({
            left: left,
            top: top,
            margin: m,
            visibility: 'visible'
        });
        $toast.fadeOut(400, removeToast);
    },
    CloseMask:function()
    {
        $(".MaskDialog").remove();
    },
    OpenMask: function (message, position) {//蒙板
        var $w = $(window), $toast = $('<div class="ui-popup MaskDialog"><div class="Mask-close"><a href=\"javascript:$.CloseMask()\"><img src="/js/jprompt/img/close.png" width="30" height="30"/></a></div><div class="MaskDialog-Content">' + message + '</div></div>');
        var top =10, left = "50%", m = "0 0 0 0", height = $(window).height() * 0.7, width = $(document).width() - 80;
        if (!isNaN(position)) {
            delay = Math.max(1000, Number(position));
        }
        else {
            delay = 2000;
        }
        var removeToast = function () {
            $(this).remove();
        };
        $toast.appendTo($(document.body)).delay(2000);
        //$toast.appendTo($(document.body));
        if (position == 'top') {
            top = 75;
        }
        else if (position == 'bottom') {
            top = $w.height() - $toast.outerHeight() / 2 - 70;
        }
        if ($w.width() == $toast.outerWidth(true)) {
            left = 0;
            //m = '-' + $toast.outerHeight() / 2 + 'px 1rem 0 1rem';
            m = '0px 1rem 10px 1rem';
        } else {
            m = '0px 0 0 -' + $toast.outerWidth() / 2 + 'px';
        }
        $toast.css({
            top: top,
            width:width,
            height: height,
            visibility: 'visible'
        });
        //$toast.show();
        //$toast.fadeOut(400000, removeToast);
    },
    /*
    *Description:重构confirmDialog特效
    *Author:xhl
    *Time:2015/06/24
    */
    confirmDialog: function (a, callback, okBtnText, cancelBtnText) {//confirm弹窗  a:提示语;（已重复修改完成）
        var $m = $(document.body);
        var dialogId = "J_ConfirmDialogId";
        var $w = $(window);
        var top = '50%', left = "50%", m = "0 0 0 0";
        var okText = okBtnText == undefined ? "确定" : okBtnText,
            cancelText = cancelBtnText == undefined ? "取消" : cancelBtnText;
        var removeToast = function () {
            $(this).remove();
        };
        if ($("#" + dialogId).length == 0) {
            var dialogHtml = '<div  class="confirmDialog" id="' + dialogId + '" >';
            dialogHtml += '<div class="confirmDialog-content">' + a + '</div>';
            dialogHtml += '<ul class="confirmDialog-btns clearfix"><li><a href="javascript:void(0)" class="ui-btn j_ConfirmCancel">' + cancelText + '</a></li><li><a href="javascript:void(0)" class="ui-btn j_ConfirmOK">' + okText + '</a></li></ul></div>';
            $(dialogHtml).appendTo($m);
            if ($w.width() == $("#" + dialogId).outerWidth(true)) {
                left = 0;
                m = '-' + $("#" + dialogId).outerHeight() / 2 + 'px 1rem 0 1rem';
            } else {
                m = '-' + $("#" + dialogId).outerHeight() / 2 + 'px 0 0 -' + $("#" + dialogId).outerWidth() / 2 + 'px';
            }
            $("#" + dialogId).css({
                left: left,
                top: top,
                margin: m
            });

            $("#" + dialogId).find(".j_ConfirmOK").bind("click", function (ev) {
                $("#" + dialogId).fadeOut(400, removeToast);
                callback();
            });
            $("#" + dialogId).find(".j_ConfirmCancel").bind("click", function (ev) {
                $("#" + dialogId).fadeOut(200, removeToast);
            });
        } else {
            $("#" + dialogId).find(".confirmDialog-content").html(a);
            $("#" + dialogId).find(".j_ConfirmOK").html(okText);
            $("#" + dialogId).find(".j_ConfirmCancel").html(cancelText);
        }
    }
});

