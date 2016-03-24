define(function (require, exports, module) {
    $.get("/assets/widget/toobar/toobar.html?t=46",function(html){
        var divObj=document.createElement("div");
        divObj.innerHTML=html;
        var first=document.body.firstChild;//得到页面的第一个元素
        document.body.insertBefore(divObj,first);//在得到的第一个元素之前插入
        page.pageBind();
        page.pageTab();
        $("#tab1").addClass("current");
    })
    var layer=require("layer");
    var common=require("common");
    var customerId=common.getQuery("customerid");
    var siteId=common.getQuery("siteId");
    var page= {
        pageBind: function () {
            var obj=$(".js-page-tab");
            $.each(obj,function(item,dom){
               $(dom).click(function(){
                   var id=$(dom).attr('id');
                   switch (id){
                       case "tab1":
                           $(".js-page-tab").removeClass("current");
                           $(".js-layout").show();
                           $(dom).addClass("current");
                           $("#tab-box-2").hide();
                           $("#tab-box-3").hide();
                           break;
                       case "tab2":
                           $(".js-page-tab").removeClass("current");
                           $("#tab-box-2").show();
                           $(".js-layout").hide();
                           $("#tab-box-3").hide();
                           $(dom).addClass("current");
                           break;
                       case "tab3":
                           $(".js-page-tab").removeClass("current");
                           $("#tab-box-3").show();
                           $(".js-layout").hide();
                           $("#tab-box-2").hide();
                           $(dom).addClass("current");
                           break;
                   }
               });
            });
        },
        pageTab:function(){
            $("#js-cms-selectPhoto").click(function(){
                layer.open({
                    type: 2,
                    title: "图片库",
                    shadeClose: true,
                    shade: 0.8,
                    closeBtn:1,
                    area: ['700px', '580px'],
                    content: "/assets/js/jPicture/photo.html?customerId="+customerId+"&isMult=false&v=1.2",
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
        }
    }
});