/**
 * Created by Neo on 2016/8/22.
 */
;(function ($){
    var HTML = [
        // '<div class="addTreeList row">',
        '<div class="form-inline col-xs-12 mb10">',
        '<div class="mb10 pull-left dropdown js-get-UrlSource">',
        '<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">链接来源 <span class="caret"></span></button>',
        '<ul class="dropdown-menu" aria-labelledby="dropdownMenu">',
        '<li><a href="javascript:void(0);" data-value="custom">自定义</a></li>',
        // '<li><a href="javascript:void(0);" data-value="0">文章</a></li>',
        // '<li><a href="javascript:void(0);" data-value="1">链接</a></li>',
        // '<li><a href="javascript:void(0);" data-value="2">视频</a></li>',
        // '<li><a href="javascript:void(0);" data-value="3">公告</a></li>',
        // '<li><a href="javascript:void(0);" data-value="4">图片</a></li>',
        // '<li><a href="javascript:void(0);" data-value="5">下载</a></li>',
        '<li><a href="javascript:void(0);" data-value="6">页面</a></li>',
        '</ul>',
        '</div>',
        '<input class="form-control mb10 tree-name" type="text" name="text" placeholder="名称">',
        '<input class="form-control mb10 tree-url" type="url" name="url" placeholder="链接">',
        '<input class="form-control mb10 tree-flag" type="hidden" name="flag" placeholder="来源">',
        '<input class="form-control mb10 tree-visibleName" type="text" name="text" placeholder="visibleName">',
        '<input class="form-control mb10 tree-visible" type="text" name="text" placeholder="visible">',
        '<input class="form-control mb10 pull-right btn btn-success js-save-node" type="button" value="保存">',
        '</div>',
        '<div class="col-xs-12">',
        '<div class="btn-group btn-group-sm clearfix" role="group">',
        '<button type="button" class="btn btn-default js-addRoot" title="添加根节点"><b class="fa fa-plus" aria-hidden="true"></b>添加根节点</button>',
        '<button type="button" class="btn btn-default js-addSibling" title="添加同级节点"><b class="fa fa-plus" aria-hidden="true"></b>添加同级节点</button>',
        '<button type="button" class="btn btn-default js-addLeaf" title="添加子级节点"><b class="fa fa-plus" aria-hidden="true"></b>添加子级节点</button>',
        '<button type="button" class="btn btn-default js-replyTree" title="重做"><b class="fa fa-reply-all" aria-hidden="true"></b>清空重做节点</button>',
        '</div>',
        '</div>',
        '<div class="col-sm-12 scrollDiv">',
        '<ul class="ztree"></ul>',
        '</div>'
        // '</div>'
    ];

    $.fn.addTreeView = function (options) {
        var s = $.extend({
            debug: false,
            visible: true,
            level: -1,
            treeNodes: [],
            extraItems: []
        }, options);
        var self = this;
        var DOM = HTML.join('\n');
        var NUM = +new Date();
        var id = 'treeView-' + NUM;

        self.addClass('addTreeList row')
            .append(DOM)
            .find('.ztree')
            .attr('id',id);

        var $DOM = $('#'+id);

        var UrlSource = $('.js-get-UrlSource');

        if(s.extraItems) {
            if($.isArray(s.extraItems)) {
                var TempDOM = $('<div></div>');
                $.each(s.extraItems, function (i ,v) {
                    var n = v.name;
                    var f = v.flag;
                    var p = v.path;
                    !n && console.warn('参数'+ (i+1) +'缺少必要的参数 "name"');
                    !f && console.warn('参数'+ (i+1) +'缺少必要的参数 "flag"');
                    !p && console.warn('参数'+ (i+1) +'缺少必要的参数 "path"');
                    if (n && f && p) {
                        var li = '<li><a href="javascript:void(0);" data-value="extra" data-flag="'+f+'" data-path="'+p+'">'+ n +'</a></li>';
                        TempDOM.append(li)
                    }
                });
                UrlSource.find('.dropdown-menu').append(TempDOM.html());
            } else {
                console.warn('TypeError: "items" is not an array');
            }
        }

        if(!s.visible) {
            self.find('.tree-visibleName').detach();
            self.find('.tree-visible').detach();
        }
        $('.js-get-UrlSource', this).find('button')
            .attr('id', 'dropdownMenu-' + NUM)
            .end()
            .find('ul')
            .attr('aria-labelledby', 'dropdownMenu-' + NUM);
        TreeView.init($DOM, id, s.treeNodes);
        TreeView.initBind();

        var treeObj = $.fn.zTree.getZTreeObj(TreeView.treeId);

        $('.js-save-node', this).click(function () {
            var selectNode = treeObj.getSelectedNodes()[0];
            if(selectNode) {
                selectNode.name = $('.tree-name').val();
                selectNode.linkPath = $('.tree-url').val();
                selectNode.flag = parseInt($('.tree-flag').val()) || 0;
                selectNode.visible = $('.tree-visible').val();
                treeObj.updateNode(selectNode);
            }
        });

        UrlSource.find('a').click(function() {
            var _this = $(this);
            var content = _this.text() + ' <span class="caret">';
            UrlSource.find('button').html(content);
            var $this = _this.parents('.js-get-UrlSource');
            var type = _this.attr('data-value');
            switch(type) {
                case 'custom':
                    clearInput($this);
                    break;
                case 'extra':
                    extraInput(_this);
                    break;
                default:
                    plugin.getUrlPopover($this, type, s.debug);
            }
        });
    };


    $.extend({
        getTreeViewData: function() { return TreeView.getNodes(); }
    });

    var setting = {
        view: {
            showLine: false,
            selectedMulti: false
        },
        edit: {
            enable: true,
            showRemoveBtn: true,
            showRenameBtn: true
        },
        data: {
            keep: {
                parent:false,
                leaf:false
            },
            simpleData: {
                enable: true,
                idKey: "id",
                pIdKey: "pId",
                rootPId: 0
            }
        },
        callback: {
            beforeEditName: beforeEditName,
            onRemove: onRemove,
            onRename: onRename,
            onClick: onClick
        }
    };
    var className = "dark";

    function onRemove(event, treeId, treeNode, clickFlag) {
        className = (className === "dark" ? "":"dark");
        var level =  treeNode.level;
        var parent = treeNode.getParentNode();
        if (level) {
            var len = parent.children.length;
            if (!len) {
                parent.isParent = false;
            }
        }
    }
    function beforeEditName(treeId, treeNode) {
        setNameAndUrl(treeNode);
    }
    function onClick(event, treeId, treeNode, clickFlag) {
        setNameAndUrl(treeNode);
    }

    function onRename(event, treeId, treeNode, clickFlag) {
        setNameAndUrl(treeNode);
    }

    function setNameAndUrl(obj) {
        var inputUrl = $('.tree-url');
        $('.tree-name').val(obj.name);
        inputUrl.val(obj.linkPath);
        $('.tree-flag').val(obj.flag);

        if (obj.flag) {
            inputUrl.attr('readonly', 'readonly');
        } else {
            inputUrl.removeAttr('readonly');
        }
        $('.tree-visibleName').val(obj.visibleName);
        $('.tree-visible').val(obj.visible);
    }

    function clearInput(element) {
        var inputName = element.siblings('.tree-name');
        var inputUrl = element.siblings('.tree-url');
        var inputFlag = element.siblings('.tree-flag');
        var inputVisibleName = element.siblings('.tree-visibleName');
        var inputVisible = element.siblings('.tree-visible');

        inputName.val('');
        inputUrl.val('');
        inputFlag.val(0);
        inputVisibleName.val('');
        inputVisible.val('');

        if (inputUrl.attr('readonly')) {
            inputUrl.removeAttr('readonly');
        }
    }

    function extraInput(element) {
        var name = element.text();
        var flag = element.data('flag');
        var path = element.data('path');

        var parent = element.parents('.js-get-UrlSource');

        var inputName = parent.siblings('.tree-name');
        var inputUrl = parent.siblings('.tree-url');
        var inputFlag = parent.siblings('.tree-flag');
        var inputVisibleName = parent.siblings('.tree-visibleName');
        var inputVisible = parent.siblings('.tree-visible');

        inputName.val(name);
        inputUrl.val(path).attr('readonly', 'readonly');
        inputFlag.val(flag);
        inputVisibleName.val('');
        inputVisible.val('');

    }
    var TreeView = {
        $DOM: null,
        treeId: null,
        nodes:[],
        init: function (DOM, id, nodes) {
            this.$DOM = DOM;
            this.treeId = id;
            this.nodes = nodes || [];
            $.fn.zTree.init(this.$DOM, setting, this.nodes);
        },
        addRoot: function () {
            var treeObj = $.fn.zTree.getZTreeObj(this.treeId);
            var newId = treeObj.getNodes().length + 1;
            treeObj.addNodes(null, {
                id:newId, pId:0,
                name:"新节点" + newId,
                linkPath:'/',
                flag: 0,
                visibleName: '',
                visible: '',
                visibleValue: ''
            });
        },
        addSibling: function () {
            var num = '';
            var treeObj = $.fn.zTree.getZTreeObj(this.treeId);

            var selectNode = treeObj.getSelectedNodes();
            var len = selectNode.length;
            if (len) {
                var parentNode = selectNode[0].getParentNode();
                if (!parentNode) {
                    num = treeObj.getNodes().length + 1;
                    treeObj.addNodes(parentNode, {
                        id: num,
                        pId: 0,
                        name: "新节点" + num,
                        linkPath:'/',
                        flag: 0,
                        visibleName: '',
                        visible: '',
                        visibleValue: ''
                    });
                } else {
                    var length = parentNode.children.length;
                    num = selectNode[0].id + length;
                    treeObj.addNodes(parentNode, {
                        id: num,
                        pId: selectNode[0].pid,
                        name:"新节点" + num,
                        linkPath:'/',
                        flag: 0,
                        visibleName: '',
                        visible: '',
                        visibleValue: ''
                    });
                }

            }
        },
        addChild: function () {
            var num = 1;
            var treeObj = $.fn.zTree.getZTreeObj(this.treeId);
            var selectNode = treeObj.getSelectedNodes();
            var len = selectNode.length;
            if (len) {
                var children = selectNode[0].children;
                if (children) {
                    num = children.length + 1;
                }
                selectNode[0].isParent = true;

                var id = selectNode[0].id * 10 + num;

                treeObj.addNodes(selectNode[0], {
                    id: id,
                    pId: selectNode[0].id,
                    name:"新节点" + id,
                    linkPath:'/',
                    flag: 0,
                    visibleName: '',
                    visible: '',
                    visibleValue: ''
                });

            }
        },
        replyTree: function () {
            var treeObj = $.fn.zTree.getZTreeObj(this.treeId);
            var len = treeObj.getNodes().length;
            if (len) {
                layer.confirm('您是否确定清空数据？',{
                    btn: ['确定', '取消']
                }, function (index) {
                    $.fn.zTree.init(TreeView.$DOM, setting, '');
                    treeNode = null;
                    layer.close(index)
                }, function (index) {
                    layer.close(index)
                });
            }
        },
        getNodes: function () {
            var treeObj = $.fn.zTree.getZTreeObj(this.treeId);
            //var nodes = treeObj.transformToArray(treeObj.getNodes());
            // return this.getJSON(nodes);
            return treeObj.getNodes();
        },
        getJSON: function (nodes) {
            var node = [];
            $.each(nodes, function (i, v) {
                var obj = {};
                obj.id = v.id;
                obj.pId = v.pId;
                obj.name = v.name;
                obj.linkPath = v.linkPath;
                obj.flag = v.flag;
                obj.isParent = v.isParent;
                node.push(obj);
            });
            return node;
        },
        initBind: function () {
            var self = this;
            $('.js-addRoot').click(function() {
                self.addRoot();
            });
            $('.js-addSibling').click(function () {
                self.addSibling();
            });
            $('.js-addLeaf').click(function () {
                self.addChild();
            });
            $('.js-replyTree').click(function () {
                self.replyTree()
            });
        }
    };

    var plugin = {
        getUrlPopover: function (element, parameter, debug) {
            var self = this;
            var url = debug ? linkMock : '/dataSource/findContentType';

            $('body').append($('#templateHtml').html());
            var $container = $('#selectDataTable');
            $('#myModalLabel').text('选择链接');
            $container.find('.modal-body').empty().append('<table id="js-url-selectTable" class="table table-bordered table-striped table-hover" width="100%"></table >');
            $container.find('.modal-footer').remove();
            $container.modal();

            TableData.createTable($('#js-url-selectTable'),
                {
                    "url": url,
                    "data": function ( d ) {
                        return $.extend( {}, d, {
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
                            "data": "serial"
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
            var inputName = element.siblings('.tree-name');
            var inputUrl = element.siblings('.tree-url');
            var inputFlag = element.siblings('.tree-flag');
            var $container = $('#selectDataTable');
            var $element = $('#js-url-selectTable');
            $element.off( 'click', '.js-choose-url');
            $element.on( 'click', '.js-choose-url', function () {
                var data = $element.DataTable().row( $(this).parents('tr') ).data();
                inputName.val(data.name);
                inputUrl.val(data.serial);
                inputFlag.val(1);
                if (!inputUrl.attr('readonly')) {
                    inputUrl.attr('readonly', 'readonly');
                }
                $container.modal('hide');
            });
        }
    }

})(jQuery);