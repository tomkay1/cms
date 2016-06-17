/**
 * Created by xhl on 2015/12/21.
 */
define(["js/jquery-1.9.1.min"],function () {
    var depthIndex=0;
    var initTreeSelect=function(data,div){
        depthIndex++;
        if(data!=null){
            for(var i=0;i<data.length;i++){
                if(data[i]!=null) {
                    if (data[i].parent== null) {
                        depthIndex = 1;
                    }
                    if (data[i].children != null && data[i].children.length > 0) {
                        var blank = "";
                        for (var j = 1; j < depthIndex; j++) {
                            blank += "&nbsp;&nbsp;&nbsp;";
                        }
                        $("#" + div).append("<option value='" + data[i].id + "'>" + blank + data[i].name + "</option>")
                        initTreeSelect(data[i].children, div);
                    } else {
                        var blank = "";
                        for (var j = 1; j < depthIndex; j++) {
                            blank += "&nbsp;&nbsp;&nbsp;";
                        }
                        $("#" + div).append("<option value='" + data[i].id + "'>" + blank + data[i].name + "</option>")
                    }
                }
            }
        }
    };
    return {
        getCategoryList:function(siteid,name,div){
            $.ajax({
                url: "/manage/category/getCategoryList",
                data: {
                    siteId:siteid,
                    name: name
                },
                type: "POST",
                dataType: 'json',
                success: function (data) {
                    if(data!=null&&data.data!=null&&data.data.length>0){
                        initTreeSelect(data.data,div);
                    }
                }
            });
        }
    }
});