$.fn.extend({
    jacksonUpload: function (options) {
        var $this = this;
        var self = this;//Grid对象
        var settings = {
            text: "上传",
            color:"#666",
            submit:true,
            name:"file",
            accept: "jpg, jpeg,png,gif,bmp",
            description: "",
            className: "jq-site-bottom",
            classLoadName: "jq-site-bottom-loadding",
            boxWidth:"600",
            loadText: "上传中..."
        };
        //后期添加新方法，支持参数方式传递
        self.createParam=function(options){
            var html=""
            var jsonData= options.data;
            for(var item in jsonData){
                html+="<input type='hidden' name='"+item+"' value='"+jsonData[item]+"'/>";
            }
            return html;
        };
        var top = this.offset().top;
        var left = this.offset().left
        var options = $.extend(settings, options);
        var temporary_iframe_id = 'temporary-iframe-' + (new Date()).getTime() + '-' + (parseInt(Math.random() * 1000));
        var temporary_form_id = 'temporary-form-' + (new Date()).getTime() + '-' + (parseInt(Math.random() * 1000));
        var temporary_file_id = 'temporary-file-' + (new Date()).getTime() + '-' + (parseInt(Math.random() * 1000));
        var temporary_input_id = 'temporary-input-' + (new Date()).getTime() + '-' + (parseInt(Math.random() * 1000));
        var temporary_descript_id = 'temporary-descript-' + (new Date()).getTime() + '-' + (parseInt(Math.random() * 1000));
        this.fileid = temporary_file_id;
        var htmlText = '';
        htmlText += '<form id="' + temporary_form_id + '" class="jq-jupload-box" method="' + options.method + '" style="top:' + top + 'px;left:' + left + 'px;width:'+boxWidth+'px;  position: absolute;" action="' + options.url + '" target="' + temporary_iframe_id + '" enctype="' + options.enctype + '">';
        htmlText += '<div class="'+options.className+'">';
        htmlText += '<span style="display: inline-block;margin-top: 5px;color: '+options.color+';font-size: 12px;" id="' + temporary_input_id + '">' + options.text + '</span>';
        htmlText += '<input type="file" id="' + temporary_file_id + '" name="' + options.name + '" accept="'+options.accept+'" style="position:absolute;left:0;top:0;width:100px;height:30px;box-shadow: 0 0 3px 0 rgba(0, 0, 0, 0.2);opacity:0;-moz-opacity: 0;filter: alpha(opacity=0);cursor:pointer"/>';
        htmlText+=self.createParam(options);
        htmlText += '</div><div class="clear"></div>';
        htmlText += '<span style="margin-left:5px;line-height:30px;" id="' + temporary_descript_id + '">' + options.description + '</span>';
        htmlText += '</form>';
        htmlText += '<iframe id="' + temporary_iframe_id + '" class="jq-jupload-box" style="position:absolute; z-index:-1; visibility: hidden;" frameborder="0" width="0" height="0" src="about:blank" name="' + temporary_iframe_id + '"></iframe>';
        $("body").append(htmlText);
        //this.html(htmlText);
        if (options.submit) {
            var ie_timeout;
            $("#" + temporary_file_id).change(function () {
                $("#" + temporary_descript_id).css({ color: "#666" });
                $("#" + temporary_input_id).html(options.loadText);
                $("." + options.className).addClass(options.classLoadName).removeClass(options.className);
                $("#" + temporary_form_id).submit();
                $("#" + temporary_file_id).attr("type", "hidden");//设置类型,使得上传过程中不能再次选择文件
            });
            $("#" + temporary_iframe_id).load(function () {
                //注意，response里面的Content-Type需要设置为html/text，否则在IE下会被另存为
                //并且，这种方法是不支持跨域访问的！！！
                var data = $(window.frames[temporary_iframe_id].document.body).text();
                $("#" + temporary_input_id).html(options.text);
                $("." + options.classLoadName).addClass(options.className).removeClass(options.classLoadName);
                $("#" + temporary_file_id).attr("type", "file");
                if (data != null && data != "") {
                    try {
                        data = $.parseJSON(data);
                        var resultMsg = options.callback(data);    //回调函数
                        //if (data.code == 200) {
                        //    $("#" + temporary_descript_id).html(data.msg);
                        //}
                        //else {
                        //    $("#" + temporary_descript_id).html(data.msg);
                        //    $("#" + temporary_descript_id).css({ color: "red" });
                        //}
                    }
                    catch (e) {
                        $("#" + temporary_descript_id).html("上传图片失败，请重试");
                        $("#" + temporary_descript_id).css({ color: "red" });
                    }
                    finally {
                        //if (ie_timeout) clearTimeout(ie_timeout)
                        //ie_timeout = null;
                    }
                }
            });
        }
        $(window).resize(function () {
            var top = $this.offset().top;
            var left = $this.offset().left
            $("#" + temporary_form_id).css({ top: top, left: left });
        });
        $(window).scroll(function () {
            var top = $this.offset().top;
            var left = $this.offset().left
            $("#" + temporary_form_id).css({ top: top, left: left });
        })
    }
});