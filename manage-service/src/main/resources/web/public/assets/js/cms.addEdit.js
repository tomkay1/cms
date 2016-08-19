/**
 * Created by Neo on 2016/8/15.
 */
;(function ($){
    var editHTML = [
        '<div class="addEditBox row">',
        '<div class="col-xs-3 mb10 <% this.hasImage ? "" : "hidden"%>">',
        '<img class="img-responsive img-thumbnail center-block js-image" src="http://placehold.it/80x80?text=1" />',
        '</div>',
        '<div class="form-inline col-xs-9 row mb10">',
        '<div class="col-xs-12 mb10 <% this.hasParagraph ? "" : "hidden"%>">',
        '<label class="mr6">文字：</label>',
        '<input class="form-control" type="text" name="text" placeholder="文字内容" />',
        '</div>',
        '<div class="col-xs-12 mb10 <% this.hasUrl ? "" : "hidden"%>">',
        '<label class="mr6">链接：</label>',
        '<select class="form-control mr6 js-get-source">',
        '<option value="0">不添加链接</option>',
        '<option value="1">文章链接</option>',
        '<option value="2">新闻链接</option>',
        '<option value="3">页面链接</option>',
        '<option value="custom">自定义链接</option>',
        '</select>',
        '<input class="form-control" type="url" name="url" placeholder="图文链接" readonly>',
        '</div>',
        '</div>',
        '<div class="form-group col-xs-12 <% this.hasTextArea ? "" : "hidden"%>">',
        ' <textarea class="form-control" rows="3" placeholder="详细内容"></textarea>',
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
                ele.find('.js-image').attr('src', v.uri);
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
                    case 0:
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
            hasParagraph: true,
            hasUrl: false,
            hasTextArea: false,
            customHTML: ''
        }, options);
        var self = this;
        var DOM = methods.create(s);
        self.click(function () {
            plugin.popover(self, DOM, true);
        });
    };

    var plugin = {
        uploadCallBackData : [],
        popover: function(pointer, html, flag) {
            var self = this;

            $('body').append($('#templateHtml').html());
            var $container = $('#selectDataTable');
            uploadForm({
                ui: '#js-fileUploader',
                inputName: 'myfile',
                maxFileCount: 1,
                successCallback: function(files, data, xhr, pd ) {
                    var temp = {};
                    temp.uri = data.fileUri;
                    self.uploadCallBackData.push(temp);
                }
            });
            this.switchButton();

            $container.modal();

            this.checkImage(flag, pointer, html);

            $container.on('hide.bs.modal', function () {
                $(this).off('hide.bs.modal');
                $(this).remove();
            });
        },
        checkImage: function(flag, pointer, html) {
            var $table = $('#js-selectTable');
            $('#selectDataTable').addClass('imageTable-style');
            $table.bootstrapTable('destroy').bootstrapTable({
                url: 'test.json',
                dataType: 'json',
                locales: 'zh-CN',
                showHeader:false,
                striped: true,
                pagination: true,
                search: true,
                clickToSelect: true,
                pageSize: 16,
                pageList: [16, 24, 32],
                columns: [
                    {
                        class: "checkboxBtn",
                        checkbox: flag,
                        radio: !flag
                    },
                    {
                        class: "pictureBox",
                        title: '序号',
                        field: 'uri',
                        formatter: function(value, row, index){
                            return '<img  src="'+value+'" class="pictureImages img-rounded" >';
                        }
                    }
                ]
            });
            this.selectData($table, pointer, html);
            this.selectUpload(pointer, html)
        },
        selectData: function (element, pointer, html) {
            var $container = $('#selectDataTable');
            var $ele = $container.find('.js-select-btn');
            $ele.off('click');
            $ele.on('click', function () {
                var Data = element.bootstrapTable('getAllSelections');
                methods.init(pointer, html, Data);
                $container.modal('hide')
            });
        },
        selectUpload: function (pointer, html) {
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
            $('body').append($('#templateHtml').html());
            var $container = $('#selectDataTable');
            $('#myModalLabel').text('选择链接');
            $container.find('.modal-body').empty().append('<table id="js-url-selectTable"></table >');
            $container.find('.modal-footer').remove();
            $container.modal();

            this.getResourceLocator(element, parameter);

            $container.on('hide.bs.modal', function () {
                $(this).off('hide.bs.modal');
                $(this).remove();
            });
        },
        getResourceLocator: function (element, parameter) {
            var self = this;
            var $table = $('#js-url-selectTable');
            $table.bootstrapTable('destroy').bootstrapTable({
                url: 'data.json',
                dataType: 'json',
                locales: 'zh-CN',
                striped: true,
                pagination: true,
                search: true,
                clickToSelect: true,
                pageSize: 10,
                pageList: [10, 15],
                columns: [
                    {
                        width: '50%',
                        title: '数据名字',
                        field: 'name'
                    },
                    {
                        width: '30%',
                        title: '创建时间',
                        field: 'date'
                    },
                    {
                        width: '20%',
                        title: '操作',
                        align: 'center',
                        field: 'url',
                        formatter:function(value, row, index){
                            return '<button class="btn btn-default btn-xs js-choose-url" data-url="'+ row.url +'">选取</button>';
                        },
                        events: 'actionEvents'
                    }
                ]
            });
            window.actionEvents = {
                'click .js-choose-url': function (e, value, row, index) {
                    self.chooseUrl(element, value);
                }
            };
        },
        chooseUrl: function (element, value) {
            var $container = $('#selectDataTable');
            var input = element.siblings('input[type="url"]');
            input.val(value);
            $container.modal('hide');
        }
    };
})(jQuery);
