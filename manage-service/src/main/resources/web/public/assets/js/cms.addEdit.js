/**
 * Created by Neo on 2016/8/15.
 */
;(function ($){
    var editHTML = [
        '<div class="addEditBox row">',
        '<div class="col-xs-3 mb10 <% this.hasImage ? "" : "hidden"%>">',
        '<img class="img-responsive img-thumbnail center-block js-image <% this.imageClass %>" src="http://placehold.it/80x80?text=1" />',
        '</div>',
        '<div class="form-inline col-xs-9 row mb10">',
        '<div class="col-xs-12 mb10 <% this.hasParagraph ? "" : "hidden"%>">',
        '<label class="mr6">文字：</label>',
        '<input class="form-control <% this.paragraphClass %>" type="text" name="text" placeholder="文字内容" />',
        '</div>',
        '<div class="col-xs-12 mb10 <% this.hasUrl ? "" : "hidden"%>">',
        '<label class="mr6">链接：</label>',
        '<select class="form-control mr6 js-get-source">',
        '<option value="noUrl">不添加链接</option>',
        '<option value="6">页面资源</option>',
        '<option value="0">文章资源</option>',
        '<option value="1">链接资源</option>',
        '<option value="2">视频资源</option>',
        '<option value="2">视频资源</option>',
        '<option value="3">公告资源</option>',
        '<option value="5">下载资源</option>',
        '<option value="custom">自定义链接</option>',
        '</select>',
        '<input class="form-control <% this.urlClass %>" type="url" name="url" placeholder="图文链接" readonly>',
        '</div>',
        '</div>',
        '<div class="form-group col-xs-12 <% this.hasTextArea ? "" : "hidden"%>">',
        '<textarea class="form-control <% this.textArea %>" rows="3" placeholder="详细内容"></textarea>',
        '</div>',
        '<div class="btn-group btn-group-xs" role="group">',
        '<div class="btn btn-default js-move-edit" title="移动"><b class="fa fa-arrows" aria-hidden="true"></b></div>',
        '<button type="button" class="btn btn-default js-delete-edit" title="删除"><b class="fa fa-trash-o" aria-hidden="true"></b></button>',
        '</div>',
        '</div>'
    ];

    var TemplateEngine = function(html, options) {
        var re = /<%([^%>]+)?%>/g, reExp = /(^( )?(if|for|else|switch|case|break|{|}))(.*)?/g, code = 'var r=[];\n', cursor = 0;
        var add = function(line, js) {
            js? (code += line.match(reExp) ? line + '\n' : 'r.push(' + line + ');\n') :
                (code += line != '' ? 'r.push("' + line.replace(/"/g, '\\"') + '");\n' : '');
            return add;
        }
        while(match = re.exec(html)) {
            add(html.slice(cursor, match.index))(match[1], true);
            cursor = match.index + match[0].length;
        }
        add(html.substr(cursor, html.length - cursor));
        code += 'return r.join("");';
        return new Function(code.replace(/[\r\t\n]/g, '')).apply(options);
    };

    var methods = {
        delete: function() {
            $(document).on('click', '.js-delete-edit', function() {
                var element = $(this).parents('.addEditBox');
                element.remove();
            });
        },
        move: function() {
            $('.borderBoxs').sortable({
                connectWith: '.borderBoxs',
                handle: '.js-move-edit'
            });
        },
        create: function(obj) {
            var DOM = '';
            if(obj.customHTML == '') {
                var html = editHTML.join('\n');
                DOM = TemplateEngine(html,obj);
            } else {
                DOM = obj.customHTML
            }
            return DOM;
        },
        onClick: function () {
            $(document).on('click', '.js-image', function() {
                var self = $(this);
                plugin.popover(self, '', false);
            });
        },
        addEdit: function (ele, html, data) {
            var content = $('<div></div>');
            $.each(data, function (i, v) {
                var ele = $(html).clone();
                ele.find('.js-image').attr('src', v.url);
                content.append(ele);
            });
            ele.before(content.html());
            this.delete();
            this.move();
            this.onClick();
            this.selectData();
        },
        selectData: function () {
            var self = this;
            $(document).on('change', '.js-get-source',function () {
                var $this = $(this);
                var type = $this.val();

                switch(type) {
                    case 'noUrl':
                        self.addReadonly($this);
                        break;
                    case 'custom':
                        self.removeReadonly($this);
                        break;
                    default:
                        plugin.getUrlPopover($this, type);
                }
            });
        },
        removeReadonly: function (element) {
            var input = element.siblings('input[type="url"]');
            input.removeAttr('readonly');
        },
        addReadonly: function (element) {
            var input = element.siblings('input[type="url"]');
            input.val('');
            if (!input.attr('readonly')) {
                input.attr('readonly', 'readonly');
            }
        },
        changeImage: function (ele, data) {
            var uri = data[0].uri;
            ele.attr('src', uri)
        },
        init: function(ele, html, data) {
            if (data.length && html) {
                this.addEdit(ele, html, data);
            }
            if (data.length && !html) {
                this.changeImage(ele, data);
            }
        }
    };
    $.fn.addEdit = function (options) {
        var s = $.extend({
            hasImage: true,
            imageClass:'',
            hasParagraph: false,
            paragraphClass: '',
            hasUrl: false,
            urlClass: '',
            hasTextArea: false,
            textArea: '',
            customHTML: ''
        }, options);
        var self = this;
        var DOM = methods.create(s);
        self.off('click');
        self.on('click', function () {
            plugin.popover(self, DOM, true);
        });
    };

    var plugin = {
        uploadCallBackData : [],
        popover: function(pointer, html, flag) {
            var self = this;

            $('body').append($('#templateHtml').html());
            var $container = $('#selectDataTable');
            self.switchButton();

            self.initUpload();
            self.changeImage(pointer, html);

            $container.modal();

            self.getImageData(pointer, html, flag);

            $container.on('hide.bs.modal', function () {
                $(this).off('hide.bs.modal');
                $(this).remove();
            });


        },
        getImageData: function(pointer, html, flag) {
            var self = this;
            $('#selectDataTable').addClass('imageTable-style');

            TableData.createTable($('#js-selectTable'),
                {
                    "url": '/dataSource/findContentType',
                    "data": function ( d ) {
                        return $.extend( {}, d, {
                            "pageId": 1,
                            "contentType": 5
                        });
                    }
                }, flag, {
                    "columns": [
                        {
                            "data": "url"
                        }
                    ],
                    "columnDefs": [
                        {
                            "className": "pictureBox",
                            "render": function (data, type, row ) {
                                return '<img  src="'+data+'" class="pictureImages img-rounded" >';
                            },
                            "targets": 0
                        }
                    ],
                    "lengthMenu": [11, 22, 33],
                    "displayLength": 11
                },function (data) {
                    self.creatEditArea(pointer, html, data)
                });
        },
        initUpload: function () {
            var self = this;
            uploadForm({
                ui: '#js-fileUploader',
                inputName: 'myfile',
                maxFileCount: 1,
                successCallback: function(files, data, xhr, pd) {
                    var temp = {};
                    temp.uri = data.fileUri;
                    self.uploadCallBackData.push(temp);
                }
            });
        },
        creatEditArea: function (pointer, html, data) {
            var $container = $('#selectDataTable');
            methods.init(pointer, html, data);
            $container.modal('hide')
        },
        changeImage: function (pointer, html) {
            var self = this;
            var $container = $('#selectDataTable');
            var $ele = $container.find('.js-uploader-btn');
            $ele.off('click');
            $ele.on('click', function () {
                methods.init(pointer, html, self.uploadCallBackData);
                $container.modal('hide');
                self.uploadCallBackData = [];
            });
        },
        switchButton: function () {
            $('a[data-toggle="tab"]', '#selectDataTable').on('show.bs.tab', function (e) {
                var toggle = e.target.hash;
                $('.modal-footer').find('.btn-primary').each(function () {
                    if ($(this).attr('data-toggle') == toggle) {
                        $(this).removeClass('hidden');
                        $('.modal-footer').find('.btn-primary').not($(this)).addClass('hidden');
                    }
                });
            })
        },
        getUrlPopover: function (element, parameter) {
            var self = this;
            $('body').append($('#templateHtml').html());
            var $container = $('#selectDataTable');
            $('#myModalLabel').text('选择链接');
            $container.find('.modal-body').empty().append('<table id="js-url-selectTable" class="table table-bordered table-striped table-hover" width="100%"></table >');
            $container.find('.modal-footer').remove();
            $container.modal();

            TableData.createTable($('#js-url-selectTable'),
                {
                    "url": '/dataSource/findContentType',
                    "data": function ( d ) {
                        return $.extend( {}, d, {
                            "pageId": 1,
                            "contentType": parameter
                        });
                    }
                }, '', {
                    "columns": [
                        {
                            "title": "资源名称",
                            "data": "name"
                        },
                        {
                            "title": "创建日期",
                            "data": "date"
                        },
                        {
                            "title": "资源链接",
                            "data": "url"
                        },
                        {
                            "title": "操作",
                            "data": "null",
                            "defaultContent": '<button class="btn btn-default btn-xs js-choose-url" type="button">选取</button>'
                        }
                    ],
                    "columnDefs": [
                        {
                            "className": "text-center",
                            "targets": -1
                        }
                    ],
                    "lengthMenu": [10, 15, 20],
                    "displayLength": 10
                });
            self.chooseUrl(element);
            $container.on('hide.bs.modal', function () {
                $(this).off('hide.bs.modal');
                $(this).remove();
            });
        },
        chooseUrl: function (element) {
            var input = element.siblings('input[type="url"]');
            var $container = $('#selectDataTable');
            var $element = $('#js-url-selectTable');
            $element.off( 'click', '.js-choose-url');
            $element.on( 'click', '.js-choose-url', function () {
                var data = $element.DataTable().row( $(this).parents('tr') ).data();
                input.val(data.url);
                if (!input.attr('readonly')) {
                    input.attr('readonly', 'readonly');
                }
                $container.modal('hide');
            });
        }
    };
})(jQuery);
