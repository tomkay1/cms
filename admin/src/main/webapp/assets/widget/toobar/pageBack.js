define(function (require, exports, module) {
    var layer=require("layer");
    var common=require("common");
    var ownerId=common.getQuery("ownerId");
    var siteId=common.getQuery("siteId");
    $("#js-cms-selectPhoto").click(function(){
        alert('fssss');
        layer.open({
            type: 2,
            title: "图片库",
            shadeClose: true,
            shade: 0.8,
            closeBtn:1,
            area: ['580px', '500px'],
            content: "/assets/js/jPicture/photo.html?ownerId="+ownerId+"&isMult=false",
            //btn:["确定"],
            end: function(index, layero){
                var jsonStr=$("#js_cms_picture_value").val();
                var obj=JSON.parse(jsonStr);
                if(typeof obj!=="undefined"){
                    for(var i=0;i<obj.length;i++){
                        $("#backGround").val(obj[i].localUri);
                    }
                }
            }
        });
    });
});