define(function (require, exports, module) {
    $.get("/assets/widget/toobar/toobar.html",function(html){
        var divObj=document.createElement("div");
        divObj.innerHTML=html;
        var first=document.body.firstChild;//得到页面的第一个元素
        document.body.insertBefore(divObj,first);//在得到的第一个元素之前插入
        page.pageBind();
    })
    var layer=require("layer");
    var page= {
        pageBind: function () {
            var obj = $(".js-widget-toobar");
            $.each(obj, function (item, dom) {
                $(dom).click(function () {
                    var widgetUrl = $(dom).data('url');
                    var title = $(dom).html();
                    //window.console.log(widgetUrl);
                    layer.open({
                        type: 2,
                        title: title,
                        shadeClose: true,
                        shade: 0.8,
                        area: ['600px', '400px'],
                        content: widgetUrl
                    });
                });
            })
        }
    }
});