/**
 * Created by xhl on 2016/3/31.
 */
var JQueue = [];
var aElement = new Array();
(function ($) {
    $.extend(JQueue, {
        /**
         * 元素入队
         * @param vElement vElement元素列表,每个元素(必须包含ID唯一属性)
         * @returns {number} 返回当前队列元素个数
         */
        putQueue: function (vElement) {
            if (arguments == undefined && arguments.length == 0)
                return -1; //元素入队
            for (var i = 0; i < arguments.length; i++) {
                var _index = JQueue.findIndex(arguments[i].layoutId);
                if (_index == -1) {//不存在则新增
                    aElement.push(arguments[i]);
                }
                else {//存在则修改
                    JQueue.patchQueue(arguments[i]);
                }
            }
            return aElement.length;
        },
        /**
         * 把布局列表丢入队列
         *
         * @param layoutList 布局列表
         */
        putQueueList:function(layoutList){
            for(var i=0;i<layoutList.length;i++){
                JQueue.putQueue(layoutList[i]);
            }
        },

        /**
         *
         * 元素出队
         *
         * @returns {*} vElement, 当队列元素为空时,返回null
         */
        deQueue: function () {
            if (aElement != null && aElement.length == 0)
                return null;
            else
                return aElement.shift();
        },
        /**
         * @brief: 获取队列元素个数
         * @return: 元素个数
         */
        getSize: function () {
            return aElement.length;
        },
        /**
         * @brief: 返回队头素值
         * @return: vElement
         * @remark: 若队列为空则返回null
         */
        getHead: function () {
            if (aElement != null && aElement.length == 0)
                return null;
            else
                return aElement[0];
        },
        /**
         * @brief: 返回队尾素值
         * @return: vElement
         * @remark: 若队列为空则返回null
         */
        getLast: function () {
            if (aElement != null && aElement.length == 0)
                return null;
            else
                return aElement[aElement.length - 1];
        },
        /**
         * @brief: 将队列置空
         */
        empty: function () {
            aElement.length = 0;
        },
        /**
         * @brief: 判断队列是否为空
         * @return: 队列为空返回true,否则返回false
         */
        isEmpty: function () {
            if (aElement == null || aElement.length == 0)
                return true;
            else
                return false;
        },
        /**
         * @brief: 将队列元素转化为字符串
         * @return: 队列元素字符串
         */
        toString: function () {
            var sResult = (aElement.reverse()).toString();
            aElement.reverse()
            return sResult;
        },
        toJson: function () {
            return JSON.stringify(aElement);
        },
        toLayoutList:function(){
          return aElement;
        },

        /**
         * 把控件主体放入布局队列中，存在则修改
         *
         * @param layoutId 布局ID
         * @param position 布局模块位置索引
         * @param widget 控件主体设置信息对象
         */
        putQueueLayoutWidget:function(layoutId,position,widget){
            var layout=JQueue.find(layoutId);
            if(layout!=null&&layout.module!=null){
                if(layout.module.length>0){
                    for(var i=0;i<layout.module.length;i++){
                        if(layout.module[i].position==position){
                            if(layout.module[i].widget==null){
                                layout.module[i].widget=new Array();
                            }
                            layout.module[i].widget.push(widget);
                        }
                    }
                }
                JQueue.patchQueue(layout);
            }
        },
        /**
         * 修改布局中的组件信息对象
         * @param widget 控件主体设置信息对象
         */
        patchQueueLayoutWidget:function(widget){
            var layout=JQueue.find(widget.layoutId);
            if(layout!=null&&layout.module!=null){
                if(layout.module.length>0){
                    for(var i=0;i<layout.module.length;i++){
                        if(layout.module[i].position==widget.layoutPosition){
                            if(layout.module[i].widget!=null&&layout.module[i].widget.length>0){
                                for(var j=0;j<layout.module[i].widget.length;j++){
                                    if(layout.module[i].widget[j].guid==widget.guid){
                                        layout.module[i].widget[j]=widget;
                                    }
                                }
                            }
                        }
                    }
                }
                JQueue.patchQueue(layout);
            }
        },
        /**
         * 查找布局下面的控件主体是否存在
         *
         * @param layoutId 布局ID
         * @param position 布局位置索引
         * @param widgetGuid 控件主体唯一标识
         * @returns {*} widget|-1
         */
        findLayoutWdigetByPositionAndWidgetId:function(layoutId,position,widgetGuid){
            var layout=JQueue.find(layoutId);
            if(layout!=null&&layout.module!=null){
                if(layout.module.length>0){
                    for(var i=0;i<layout.module.length;i++){
                        if(layout.module[i].position==position){
                            if(layout.module[i].widget!=null&&layout.module[i].widget.length>0){
                                for(var j=0;j<layout.module[i].widget.length;j++){
                                    if(layout.module[i].widget[j].guid==widgetGuid){
                                        return layout.module[i].widget[j];
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return -1;
        },
        /**
         * 布局排序,上移
         *
         * @param currentLayoutId 当前布局ID
         * @param prevLayoutId 上一个布局ID
         */
        layoutUp:function(currentLayoutId,prevLayoutId){
            var currentLayoutIndex=JQueue.findIndex(currentLayoutId);
            var prevLayoutIdIndex=JQueue.findIndex(prevLayoutId);
            var currentLayout=JQueue.find(currentLayoutId);
            var prevLayout=JQueue.find(prevLayoutId);
            if(currentLayoutIndex!=-1&&prevLayoutIdIndex!=-1){
                aElement[currentLayoutIndex]=prevLayout;
                aElement[prevLayoutIdIndex]=currentLayout;
            }
            //window.console.log(aElement);
        },

        /**
         * 将队列元素转化为字符串
         */
        orderToJson: function () {
            //return JSON.stringify(aElement);
            var orderElement = new Array();
            var obj = $("div[data-control='ui']");
            $.each(obj, function (item, dom) {
                var id = $(dom).attr("id");
                var resources = $(dom).attr("data-smartresources");
                var elemet = JQueue.find(id);
                if (elemet != null)
                    orderElement[item] = elemet;
                else {
                    var obj = {
                        ID: id,
                        smartResource: resources,
                        isDelete: false//SmartUI for app the version 2.2.0.0 new add ---2016-03-03
                    }
                    orderElement[item] = obj;
                    //JQueue.PutQueue(obj);//把配置信息放入队列中方便后期保存时调用各参数
                }

            });
            //SmartUI for app the version 2.2.0.0 to add
            for (var i = 0; i < aElement.length; i++) {
                //window.console.log("1->" + aElement[i].ID);
                var _index = JQueue.findByArray(orderElement, aElement[i].layoutId);
                if (_index == null) {//不存在则新增
                    aElement[i].isDelete = true;
                    orderElement.push(aElement[i]);
                }
            }
            return JSON.stringify(orderElement);
        },

        /**
         * 根据队列布局唯一ID来查找该队列布局元素所在索引信息
         * @param id  (队列元素的布局唯一标识)
         * @returns {number} 返回索引ID
         */
        findIndex: function (id) {
            for (var i = 0; i < aElement.length; i++) {
                if (aElement[i] != null && aElement[i].length != 0 && aElement[i].layoutId.length != 0) {
                    if (aElement[i].layoutId == id) {
                        return i;
                    }
                }
            }
            return -1;
        },
        /**
         * 根据队列布局唯一ID来查找该队列元素信息
         * @param id(队列元素的布局唯一标识)
         * @returns {*} 返回布局信息对象
         */
        find: function (id) {
            for (var i = 0; i < aElement.length; i++) {
                if (aElement[i] != null && aElement[i].length != 0 && aElement[i].layoutId.length != 0) {
                    if (aElement[i].layoutId == id) {
                        return aElement[i];
                    }
                }
            }
            return null;
        },

        /**
         * 查找数组中指定的ID 的布局信息对象
         * @param array 布局列表信息
         * @param id 布局唯一标识ID
         * @returns {*} 返回查找到的布局信息对象
         */
        findByArray: function (array, id) {
            for (var i = 0; i < array.length; i++) {
                if (array[i] != null && array[i].length != 0 && array[i].layoutId.length != 0) {
                    if (array[i].layoutId == id) {
                        return array[i];
                    }
                }
            }
            return null;
        },
        /**
         * 布局信息物理删除
         * @param id 布局唯一标识ID
         */
        delete: function (id) {
            for (var i = 0; i < aElement.length; i++) {
                if (aElement[i] != null && aElement[i].length != 0 && aElement[i].layoutId.length != 0) {
                    if (aElement[i].layoutId == id) {
                        aElement.splice(i, 1);
                    }
                }
            }
        },
        /**
         * 删除控件主体信息
         *
         * @param widget 控件主体信息列表
         * @param widgetGuid 控件主体唯一标识ID
         * @returns {*} 返回删除后的控件主体信息
         */
        deleteWidget:function(widget,widgetGuid){
            for (var i = 0; i < widget.length; i++) {
                if (widget[i] != null && widget[i].length != 0 && widget[i].id.length != 0) {
                    if (widget[i].guid == widgetGuid) {
                        widget.splice(i, 1);
                    }
                }
            }
            return widget;
        },
        /**
         * 删除指定的布局对象中的控件主体ID
         *
         * @param layoutId 布局唯一标识ID
         * @param widgetGuid 控件主体唯一标识ID->GUID
         * @param position  控件主体所在的模块索引
         */
        deleteWidgetByLayout:function(layoutId,widgetGuid,position) {
            var layout = JQueue.find(layoutId);
            if(layout!=null&&layout.module!=null){
                if(layout.module.length>0){
                    for(var i=0;i<layout.module.length;i++){
                        if(layout.module[i].position==position){
                            layout.module[i].widget=JQueue.deleteWidget(layout.module[i].widget,widgetGuid);
                        }
                    }
                }
                JQueue.patchQueue(layout);
            }
        },
        /**
         * 从控件主体列表中查找指定的控件主体信息对象所在的索引
         * @param widget 控件主体信息列表
         * @param widgetGuid 控件主体唯一标识->Guid
         * @returns {number} 返回查找到的索引位置
         */
        findIndexByWidget:function(widget,widgetGuid){
            for (var i = 0; i < widget.length; i++) {
                if (widget[i] != null && widget[i].length != 0 && widget[i].guid.length != 0) {
                    if (widget[i].guid == widgetGuid) {
                        return i;
                    }
                }
            }
            return -1;
        },
        /**
         * 从控件主体列表中查找指定的控件主体信息
         * @param widget 控件主体列表
         * @param widgetGuid 控件主体唯一标识->Guid
         * @returns {*} 返回查找到的控件主体信息对象
         */
        findByWidget:function(widget,widgetGuid){
            for (var i = 0; i < widget.length; i++) {
                if (widget[i] != null && widget[i].length != 0 && widget[i].guid.length != 0) {
                    if (widget[i].guid == widgetGuid) {
                        return widget[i];
                    }
                }
            }
            return null;
        },
        /**
         * 根据布局信息和控件主体唯一标识查找控件主体所在的索引
         * @param layoutId 控件主体所在的布局唯一标识ID
         * @param position 控件主体模块索引位置信息
         * @param widgetGuid 控件主体唯一标识ID->Guid
         * @returns {*} 返回控件主体所在布局列表中的索引信息
         */
        findIndexByWidgetAndLayout:function(layoutId,position,widgetGuid){
            var layout = JQueue.find(layoutId);
            if(layout!=null&&layout.module!=null){
                if(layout.module.length>0){
                    for(var i=0;i<layout.module.length;i++){
                        if(layout.module[i].position==position){
                            var widget=layout.module[i].widget;
                            return JQueue.findIndexByWidget(widget,widgetGuid);
                        }
                    }
                }
            }
            return -1;
        },
        /**
         * 根据布局和控件主体唯一标识获得控件主体信息对象
         * @param layoutId 布局唯一标识ID
         * @param position 布局模块位置索引标识
         * @param widgetGuid 控件主体唯一标识ID->GUID
         * @returns {*} 返回控件主体信息对象
         */
        findByWidgetAndLayout:function(layoutId,position,widgetGuid){
            var layout = JQueue.find(layoutId);
            if(layout!=null&&layout.module!=null){
                if(layout.module.length>0){
                    for(var i=0;i<layout.module.length;i++){
                        if(layout.module[i].position==position){
                            var widget=layout.module[i].widget;
                            return JQueue.findByWidget(widget,widgetGuid);
                        }
                    }
                }
            }
            return null;
        },
        /**
         *  布局里面的控件主体排序
         * @param layoutId 布局ID
         * @param position 所在布局中的位置索引
         * @param currentWidgetGuid 当前的控件主体唯一标识ID
         * @param prevWidgetGuid 要调换位置的控件主体唯一标识ID
         * @returns {*}
         * */
        widgetExChangeByLayout:function(layoutId,position,currentWidgetGuid,prevWidgetGuid){
            var layout = JQueue.find(layoutId);
            if(layout!=null&&layout.module!=null){
                if(layout.module.length>0){
                    for(var i=0;i<layout.module.length;i++){
                        if(layout.module[i].position==position){
                            var widget=layout.module[i].widget;
                            var currentWidgetIndex=JQueue.findIndexByWidgetAndLayout(layoutId,position,currentWidgetGuid);
                            var prevWidgetIdIndex=JQueue.findIndexByWidgetAndLayout(layoutId,position,prevWidgetGuid);
                            var currentWidget=JQueue.findByWidgetAndLayout(layoutId,position,currentWidgetGuid);
                            var prevWidget=JQueue.findByWidgetAndLayout(layoutId,position,prevWidgetGuid);
                            if(currentWidgetIndex!=-1&&prevWidgetIdIndex!=-1){
                                widget[currentWidgetIndex]=prevWidget;
                                widget[prevWidgetIdIndex]=currentWidget;
                                layout.module[i].widget=widget;
                            }
                        }
                    }
                }
            }
            JQueue.patchQueue(layout);
        },
        /**
         *队列（布局）假删除,标注删除状态
         * @param id 布局唯一标识ID
         * @return {*} 没有返回值
         */
        remove: function (id) {
            for (var i = 0; i < aElement.length; i++) {
                if (aElement[i] != null && aElement[i].length != 0 && aElement[i].layoutId.length != 0) {
                    if (aElement[i].layoutId == id) {
                        aElement[i].isDelete = true;
                        JQueue.patchQueue(arguments[i]);
                    }
                }
            }
        },
        /**
         *根据队列唯一ID来修改该队列元素信息
         *@param: vElement元素(必须包含ID唯一属性)
         *@return:返回队列修改后的元素
         */
        patchQueue: function (vElement) {
            if (vElement == null || vElement.length == 0 || vElement.layoutId.length == 0)
                return null;
            else {
                var Index = JQueue.findIndex(vElement.layoutId);
                aElement[Index] = vElement;
                return aElement[Index];
            }
        }
    })
})(jQuery);