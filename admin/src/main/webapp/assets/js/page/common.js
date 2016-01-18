/**
 * Created by Administrator on 2015/12/21.
 */
define(["js/jquery-1.9.1.min"],function () {
    return {
        calcuHeight:function(id){
            var height = $(document).height();
            $("#"+id).height(height);
        },
        calcuHeightToTop:function(id,topHeight)
        {
            var height = $(document).height()-topHeight;
            $("#"+id).height(height);
        },
        cacleHeightByIframe:function(id,topHeight)
        {
            var height = document.body.clientHeight -topHeight;
            $("#"+id).height(height);
        },
        calcuWidth:function(id,leftWidth){
            var win = $(window);
            var width = win.width() - leftWidth;
            $("#"+id).width(width);
        },
        setDisabled: function (id) {
            var disabledText = $("#" + id).attr("data-submiting-text");
            $("#" + id).addClass("disabled");
            $("#" + id).val(disabledText);
        },
        cancelDisabled: function (id) {
            //setTimeout(function () {
            var abledText = $("#" + id).attr("data-text");
            $("#" + id).removeClass("disabled");
            $("#" + id).val(abledText);
            //}, 2000);
        },
        redirectUrl:function(url){
            parent.frames["content"].src=url;
            //alert("fff");
            //parent.frames["content"].window.href=url;
        },
        /*
        * @brief 获得页面参数
        * @param 参数名
        * */
        getQuery:function(name) {
            var strHref = window.document.location.href;
            var intPos = strHref.indexOf("?");
            var strRight = strHref.substr(intPos + 1);
            var arrTmp = strRight.split("&");
            for (var i = 0; i < arrTmp.length; i++) {
                var arrTemp = arrTmp[i].split("=");
                if (arrTemp[0].toUpperCase() == name.toUpperCase()) return arrTemp[1];
            }
            if (arguments.length == 1)
                return "";
            if (arguments.length == 2)
                return arguments[1];
        },
        getSiteList:function(customerId,div){
            $.ajax({
                url: "/category/getSiteList",
                data: {
                    customerId:customerId
                },
                async:false,
                type: "POST",
                dataType: 'json',
                success: function (data) {
                    if(data!=null){
                        if(data.data!=null&&data.data.length>0){
                            for(var i=0;i<data.data.length;i++){
                                $("#"+div).append("<option value='"+data.data[i].siteId+"'>"+data.data[i].name+"</option>")
                            }
                        }else{
                            switch (data.code){
                                case 200:
                                    $("#"+div).append("<option value='-1'>还没有站点信息</option>")
                                    break;
                                case 404:
                                    $("#"+div).append("<option value='-1'>还没有站点信息</option>")
                                    break;
                                case 502:
                                    layer.msg("服务器繁忙,加载站点信息失败",{time: 2000});
                                    break;
                            }
                        }
                    }
                    else{
                        layer.msg("服务器繁忙,加载站点信息失败",{time: 2000});
                        $("#"+div).append("<option value='-1'>还没有站点信息</option>")
                    }
                }
            });
        }
    }
});