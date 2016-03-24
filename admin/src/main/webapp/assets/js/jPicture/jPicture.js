/**
 +-------------------------------------------------------------------
 * jQuery jPicture- 火图科技图片库插件
 +-------------------------------------------------------------------
 * @version 1.0.0
 * @since 2014/08/11
 * @author xhl <supper@9126.org> <http://www.9126.org/>
 +-------------------------------------------------------------------

 ======================================================== 使用说明 ================================================================
 1、使用该插件时需要调用该目录下的photo.html页面资源,并且需要带上两个参数，一下使用layer.js弹出调用图片库方式例子如下：
         layer.open({
                type: 2,
                title: "图片库",
                shadeClose: true,
                shade: 0.8,
                closeBtn:1,
                area: ['580px', '500px'],
                content: "/assets/js/jPicture/photo.html?customerId="+customerId+"&isMult=false",
                //btn:["确定"],
                end: function(index, layero){
                    var jsonStr=$("#js_cms_picture_value").val();
                    alert(jsonStr);
                }
        });
    1，content参数中是layer.js弹出请求的地址,该地址中需要两个参数
        a,customerId ---->商户ID
        b,isMult     ---->是否可以选择多张图片
 2、修改photo.html 里面的引用js和css路径,让其路径是可用状态;以下仅是例子:
     <link  href="/assets/js/jPicture/skin.css"  type="text/css" rel="stylesheet"/>
     <link  href="/assets/libs/layer/skin/layer.css"  type="text/css" rel="stylesheet"/>
     <link  href="/assets/libs/upload/jupload_skin_red.css"  type="text/css" rel="stylesheet"/>
     <script type="application/javascript" src="/assets/js/jquery-1.11.1.min.js"></script>
     <script type="application/javascript" src="/assets/libs/arttemplate/template.js"></script>
     <script type="application/javascript" src="/assets/libs/JLoad/jquery.Jload.js"></script>
     <script type="application/javascript" src="/assets/libs/layer/layer.js"></script>
     <script type="application/javascript" src="/assets/libs/layer/extend/layer.ext.js"></script>
     <script type="application/javascript" src="/assets/libs/upload/jackson-upload.js"></script>
     <script type="application/javascript" src="/assets/js/jPicture/jPicture.js"></script>

 ======================================================== 返回值说明 ================================================================
 返回分会值说明:
 该图库使用layer.js 弹出方式来体现,弹出窗口后选择一张图片或者多张图片,点击确认后,会把选中的值绑定到父窗口#js-photo-selectvalue 输入框中,没有会创建,有则修改里面的值
 弹出回调后只需要获得这个值即可,里面的值格式如下（标准的json格式,拿到值后需要转成json对象来处理选择的图片值）:
 [
   {"uri":"/resource/images/photo/3447/20160323172012.png","localUri":"http://res.huobanj.cn/resource/images/photo/3447/20160323172012.png","ID":3066},
   {"uri":"/resource/images/photo/3447/20160323155048.png","localUri":"http://res.huobanj.cn/resource/images/photo/3447/20160323155048.png","ID":3065}
 ]
 */
var _pictureTemplateBox="<div>" +
"    <div class=\"jpicture-title\">" +
"        <ul class=\"clearfix\">" +
"            <li id=\"js-picture-addFile\" data-fileid=\"0\"><a href=\"javascript:void(0)\">新建文件夹</a> </li>" +
"            <li id=\"js-picture-photo\"><a href=\"javascript:void(0)\">上传图片</a> </li>" +
"            <li class='js-picture-return' data-partent=\"0\"><a href=\"javascript:void(0)\">返回上一层</a> </li>" +
"        </ul>" +
"    </div>" +
"    <div class=\"jpicture-content\" id=\"{id}\">" +

"    </div>" +
"</div>";
var _picture_defaultid;
var _pictureDataTemplate="<ul>{{each Rows as row i}}" +
    "{{if row.PFCate==0}}" +
    "<li class=\"picture-file\" data-id=\"{{row.PFId}}\" data-parentid=\"{{row.PFatherId}}\"><img src=\"{image}\" alt=\"{{row.PFName}}\"><p>{{row.PFName}}</p></li>" +
    "{{else}}" +
    "<li class=\"picture-photo\"  data-id=\"{{row.PFId}}\" data-localurl=\"{{row.imgurl}}\" data-url=\"{{row.PFUrl}}\" data-parentid=\"{{row.PFatherId}}\">" +
    "    <img src=\"{{row.imgurl}}\" alt=\"{{row.PFName}}\">" +
    "    <div class=\"widget-image-meta\">{{row.PFSize}}</div>" +
    "    <p>{{row.PFName}}</p>" +
    "    <div class=\"selected-style\"><i class=\"icon-ok icon-white\"></i></div>" +
    "</li>" +
    "{{/if}}" +
    "{{/each}}</ul><div class='clearfix'></div> ";
$.fn.extend({
    jPicture: function (options) {
        var $element = $(this);//自身Dom对象
        var self = this;//Grid对象
        var settings = {
            label: $element.attr("id"),
            url:"http://devmallapi.huobanj.cn",//伙伴商城 mallAPI 根目录URI
            isMult:true,//是否可以选择多张图片
            customerId:"3677", //商户ID
            fileImg:"/assets/js/jPicture/w01.png",
            pageSize:"24",
            uploadUrl:"",//上传图片api接口
            array:[]
        };
        var ops = $.extend(settings, options);
        $element.data('_picture', ops);
        self.init=function(){
            var $this = $(this).data('_picture');
            _picture_defaultid=new Date().getTime();
            var json={id:_picture_defaultid};
            var html=self.resolveTemplate(json,_pictureTemplateBox);
            $("#"+$this.label).html(html);
            $element.resolveDataTemplate(0);
            self.bind();
            self.addFile();
            self.uploadPhoto();
        };
        self.resolveData=function(option,template,fileid){
            $("#" + _picture_defaultid).Jload({
                url:option.url+"/gallery/getphotofile",
                method: 'GET',
                dataType: "jsonp",
                msgImg: "/assets/js/jPicture/w01.png",
                data: { page: 1, pagesize: option.pageSize,fileid:fileid,customerid:option.customerId},
                Templete: template,
                isArtTemplete: true,
                scorllBox:""
            },function() {
                var obj=$(".picture-file");
                $.each(obj,function(item,dom){
                    $(dom).click(function(){
                        var fileId=$(dom).data("id");
                        var parentId=$(dom).data("parentid");
                        $("input[name='fileId']").val(fileId);
                        $("#js-picture-addFile").attr("data-fileid",fileId);
                        JQueue.PutQueue(parentId);
                        $element.data('_picture', option);
                        $("#"+option.label).resolveDataTemplate(fileId);
                    });
                })
                self.selectBind(option);
            });
        }
        $.fn.resolveDataTemplate=function(fileid){
            var $this = $(this).data('_picture');
            var options=$this;
            var template=self.resolveTemplate({image:options.fileImg},_pictureDataTemplate);
            self.resolveData(options,template,fileid);
        }
        /**
         *Description:获取Grid配置信息对象
         */
        $.fn.getImageList = function () {
            return $(this).data('_picture').array;
        };
        self.resolveTemplate=function (json, template) {
            var _html = template;
            for (var key in json) {
                //var _old=/{key}/g;
                var _old = "{" + key + "}";
                _html = _html.replace(_old, json[key]);
            }
            return _html;
        };
        self.selectBind=function(option){
            $(".picture-photo").unbind("click");
            var obj=$(".picture-photo");
            $.each(obj,function(item,dom){
                $(dom).click(function(){
                    var dataUrl=$(dom).data('url');
                    var localUri=$(dom).data('localurl');
                    var dataId=$(dom).data("id");
                    var imageObj={uri:dataUrl,localUri:localUri,ID:dataId}
                    if(option.isMult){
                        if($(dom).find(".selected-style").is(":hidden")){
                            pQueue.PutQueue(imageObj);
                            $(dom).find(".selected-style").css("display","block");
                        }else{
                            pQueue.Delete(dataId);
                            $(dom).find(".selected-style").css("display","none");
                        }
                    }else{
                        if($(dom).find(".selected-style").is(":hidden")){
                            pQueue.Empty();
                            pQueue.PutQueue(imageObj);
                            $(".picture-photo .selected-style").css("display","none");
                            $(dom).find(".selected-style").css("display","block");
                        }else{
                            pQueue.PutQueue(imageObj);
                            $(dom).find(".selected-style").css("display","none");
                        }
                    }
                    //重新设置对象的array数据对象,用于后面获得
                    var $this = $element.data('_picture');
                    $this.array=pQueue.array;
                    $("#js-photo-selectvalue").val(JSON.stringify($this.array));
                    $element.data('_picture', ops);
                });
            });
        }
        self.uploadPhoto=function(){
            var $this = $element.data('_picture');
            $("#js-picture-photo").jacksonUpload({
                url: "/mallapi/uploadPhoto",
                enctype: "multipart/form-data",
                submit: true,
                method: "post",
                name:"fileName",
                text:"上传图片",
                color:"#ffffff",
                boxWidth:'100',
                position:"fixed",
                data: {
                    customerId: $this.customerId,
                    fileId:$("#js-picture-addFile").attr('data-fileid')
                },
                callback: function (json) {
                    $("#"+$this.label).resolveDataTemplate($("#js-picture-addFile").attr('data-fileid'));
                }
            })
        }
        self.addFile=function(){
            var $this = $element.data('_picture');
            $("#js-picture-addFile").click(function(){
                layer.prompt({
                    title: '请输入文件夹',
                    formType: 0 //prompt风格，支持0-2
                }, function(name,index){
                   var fileid= $("#js-picture-addFile").attr('data-fileid');
                    layer.close(index);
                    $.ajax({
                        type: "get",
                        dataType:"jsonp",
                        url: $this.url+'/gallery/addfile',//提交到一般处理程序请求数据
                        data: {customid:$this.customerId,extenName:name,fileid:fileid},
                        success: function (data) {
                            $("#"+$this.label).resolveDataTemplate(fileid);
                        }
                    })
                });
            });
        }
        self.bind=function(){
            var options = $(this).data('_picture');
            var obj=$(".js-picture-return");
            $.each(obj,function(item,dom){
                $(dom).click(function(){
                    var parentId=JQueue.PopQueue();
                    if(typeof parentId=="undefined") {
                        parentId = 0;
                    }
                    $("#"+options.label).resolveDataTemplate(parentId);
                });
            })
        }
        self.init();
        var pQueue={
            array:[],
            /**
             * @brief: 元素入队
             * @param: vElement元素列表,每个元素(必须包含ID唯一属性)
             * @return: 返回当前队列元素个数
             * @remark: 1.EnQueue方法参数可以多个
             * 2.参数为空时返回-1
             */
            PutQueue:function (vElement)  {
                if (arguments==undefined && arguments.length == 0)
                    return -1; //元素入队
                for (var i = 0; i < arguments.length; i++) {
                    var _index = JQueue.FindIndex(arguments[i].ID);
                    if (_index==-1) {//不存在则新增
                        pQueue.array.push(arguments[i]);
                    }
                    else {//存在则修改
                        pQueue.array.PatchQueue(arguments[i]);
                    }
                }
                return pQueue.array.length;
            },
            /**
             *@brief 队列物理删除
             */
            Delete:function(id){
                for (var i = 0; i < pQueue.array.length; i++) {
                    if (pQueue.array[i] != null && pQueue.array[i].length != 0 && pQueue.array[i].ID.length != 0) {
                        if (pQueue.array[i].ID == id) {
                            pQueue.array.splice(i, 1);
                        }
                    }
                }
            },
            /**
             *@brief:根据队列唯一ID来修改该队列元素信息
             *@param: id(队列元素的唯一标识)
             *@return:返回队列修改后的元素
             */
            FindIndex:function (id) {
                for (var i = 0; i < pQueue.array.length; i++)
                {
                    if (pQueue.array[i] != null && pQueue.array[i].length != 0 && pQueue.array[i].ID.length != 0)
                    {
                        if (pQueue.array[i].ID == id)
                        {
                            return i;
                        }
                    }
                }
                return -1;
            },
            /**
             * @brief: 将队列置空
             */
            Empty:function()  {
                pQueue.array.length = 0;
            },
        }
    }
});
var JQueue = [];
var __aElement = new Array();
(function ($) {
    $.extend(JQueue, {
        /**
         * @brief: 元素入队
         * @param: vElement元素列表,每个元素(必须包含ID唯一属性)
         * @return: 返回当前队列元素个数
         * @remark: 1.EnQueue方法参数可以多个
         * 2.参数为空时返回-1
         */
        PutQueue:function (vElement)  {
            if (arguments==undefined && arguments.length == 0)
                return -1; //元素入队
            for (var i = 0; i < arguments.length; i++) {
                var _index = JQueue.FindIndex(arguments[i].ID);
                if (_index==-1) {//不存在则新增
                    __aElement.push(arguments[i]);
                }
                else {//存在则修改
                    JQueue.PatchQueue(arguments[i]);
                }
            }
            return __aElement.length;
        },
        /**
         * @brief: 元素出队
         * @return: vElement
         * @remark: 当队列元素为空时,返回null
         */
        DeQueue:function ()  {
            if (__aElement != null && __aElement.length == 0)
                return null;
            else
                return __aElement.shift();
        },
        PopQueue:function() {
            if (__aElement != null && __aElement.length == 0)
                return null;
            else {
                var id=__aElement[__aElement.length - 1];
                JQueue.Delete(id);
                return id;
            }
        },
        /**
         * @brief: 获取队列元素个数
         * @return: 元素个数
         */
        GetSize:function () {
            return __aElement.length;
        },
        /**
         * @brief: 返回队头素值
         * @return: vElement
         * @remark: 若队列为空则返回null
         */
        GetHead:function ()  {
            if (__aElement != null && __aElement.length == 0)
                return null;
            else
                return __aElement[0];
        },
        /**
         * @brief: 返回队尾素值
         * @return: vElement
         * @remark: 若队列为空则返回null
         */
        GetLast:function ()  {
            if (__aElement != null && __aElement.length == 0)
                return null;
            else
                return __aElement[__aElement.length - 1];
        },
        /**
         * @brief: 将队列置空
         */
        Empty:function()  {
            __aElement.length = 0;
        },
        /**
         * @brief: 判断队列是否为空
         * @return: 队列为空返回true,否则返回false
         */
        IsEmpty:function ()  {
            if (__aElement == null || __aElement.length == 0)
                return true;
            else
                return false;
        },
        /**
         * @brief: 将队列元素转化为字符串
         * @return: 队列元素字符串
         */
        toString:function ()  {
            var sResult = (__aElement.reverse()).toString();
            __aElement.reverse()
            return sResult;
        },
        toJson: function () {
            return JSON.stringify(__aElement);
        },
        /**
         *@brief:根据队列唯一ID来修改该队列元素信息
         *@param: id(队列元素的唯一标识)
         *@return:返回队列修改后的元素
         */
        FindIndex:function (id) {
            for (var i = 0; i < __aElement.length; i++)
            {
                if (__aElement[i] != null && __aElement[i].length != 0)
                {
                    if (__aElement[i] == id)
                    {
                        return i;
                    }
                }
            }
            return -1;
        },
        /**
         *@brief:根据队列唯一ID来修改该队列元素信息
         *@param: id(队列元素的唯一标识)
         *@return:返回队列修改后的元素
         */
        Find:function (id) {
            for (var i = 0; i < __aElement.length; i++) {
                if (__aElement[i] != null && __aElement[i].length != 0 ) {
                    if (__aElement[i]== id) {
                        return __aElement[i];
                    }
                }
            }
            return null;
        },
        /**
         *@brief:查找数组中指定的ID
         *@param array 要查找的数组
         *@param: id(队列元素的唯一标识)
         *@return:返回队列修改后的元素
         */
        findByArray:function(array,id){
            for (var i = 0; i < array.length; i++) {
                window.console.log("2->" + array[i].ID);
                if (array[i] != null && array[i].length != 0 && array[i].ID.length != 0) {
                    if (array[i].ID == id) {
                        return array[i];
                    }
                }
            }
            return null;
        },
        /**
         *@brief 队列物理删除
         */
        Delete:function(id){
            for (var i = 0; i < __aElement.length; i++) {
                if (__aElement[i] != null && __aElement[i].length != 0) {
                    if (__aElement[i] == id) {
                        __aElement.splice(i, 1);
                    }
                }
            }
        },
        /**
         *@brief:根据队列唯一ID来修改该队列元素信息
         *@param: vElement元素(必须包含ID唯一属性)
         *@return:返回队列修改后的元素
         */
        PatchQueue: function (vElement) {
            if (vElement==null||vElement.length == 0 || vElement.ID.length==0)
                return null;
            else {
                var Index = JQueue.FindIndex(vElement.ID);
                __aElement[Index] = vElement;
                return __aElement[Index];
            }
        }
    })
})(jQuery);