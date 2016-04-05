/**
 * Created by Administrator on 2016/3/31.
 */
var JQueue = [];
var aElement = new Array();
(function ($) {
    $.extend(JQueue, {
        /**
         * @brief: 元素入队
         * @param: vElement元素列表,每个元素(必须包含ID唯一属性)
         * @return: 返回当前队列元素个数
         * @remark: 1.EnQueue方法参数可以多个
         * 2.参数为空时返回-1
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
         * @brief: 元素出队
         * @return: vElement
         * @remark: 当队列元素为空时,返回null
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
         * @brief 把控件主体放入布局队列中，存在则修改
         * */
        putQueueLayoutWidget:function(layoutId,position,widget){
            var layout=JQueue.find(layoutId);
            if(layout!=null&&layout.module!=null){
                if(layout.module.length>0){
                    for(var i=0;i<layout.module.length;i++){
                        if(layout.module[i].position==position){
                            layout.module[i].widget.push(widget);
                        }
                    }
                }
                JQueue.patchQueue(layout);
            }
        },
        /**
         *@brief 修改布局中的组件信息对象
         * */
        patchQueueLayoutWidget:function(widget){
            var layout=JQueue.find(widget.layoutId);
            if(layout!=null&&layout.module!=null){
                if(layout.module.length>0){
                    for(var i=0;i<layout.module.length;i++){
                        if(layout.module[i].position==widget.layoutPosition){
                            if(layout.module[i].widget!=null&&layout.module[i].widget.length>0){
                                for(var j=0;j<layout.module[i].widget.length;j++){
                                    if(layout.module[i].widget[j].id==widget.id){
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
         * @breaf 查找布局下面的控件主体是否存在
         * @param widget
         * @return widget|-1
         * */
        findLayoutWdigetByPositionAndWidgetId:function(layoutId,position,widgetId){
            var layout=JQueue.find(layoutId);
            if(layout!=null&&layout.module!=null){
                if(layout.module.length>0){
                    for(var i=0;i<layout.module.length;i++){
                        if(layout.module[i].position==position){
                            if(layout.module[i].widget!=null&&layout.module[i].widget.length>0){
                                for(var j=0;j<layout.module[i].widget.length;j++){
                                    if(layout.module[i].widget[j].id==widgetId){
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
         * @brief: 将队列元素转化为字符串
         * @return: 队列元素字符串
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
        initQueue: function () {
            var obj = $("div[data-control='ui']");
            $.each(obj, function (item, dom) {
                var id = $(dom).attr("id");
                var resources = $(dom).attr("data-smartresources");
                var elemet = JQueue.find(id);
                if (elemet != null)
                    aElement[item] = elemet;
                else {
                    var obj = {
                        ID: id,
                        smartResource: resources,
                        isDelete: false//SmartUI for app the version 2.2.0.0 new add ---2016-03-03
                    }
                    aElement[item] = obj;
                    //JQueue.PutQueue(obj);//把配置信息放入队列中方便后期保存时调用各参数
                }
            });
        },
        /**
         *@brief:根据队列唯一ID来修改该队列元素信息
         *@param: id(队列元素的唯一标识)
         *@return:返回队列修改后的元素
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
         *@brief:根据队列唯一ID来修改该队列元素信息
         *@param: id(队列元素的唯一标识)
         *@return:返回队列修改后的元素
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
         *@brief:查找数组中指定的ID
         *@param array 要查找的数组
         *@param: id(队列元素的唯一标识)
         *@return:返回队列修改后的元素
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
         *@brief 队列物理删除
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
         *@brief 队列假删除,标准删除状态
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
         *@brief:根据队列唯一ID来修改该队列元素信息
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

///*
//* @brief: 定义队列类
//* @remark:实现队列基本功能
//*/
//function Queue() {   //存储元素数组
//    var aElement = new Array();
//    /*
//    * @brief: 元素入队
//    * @param: vElement元素列表,每个元素(必须包含ID唯一属性)
//    * @return: 返回当前队列元素个数
//    * @remark: 1.EnQueue方法参数可以多个
//    * 2.参数为空时返回-1
//    */
//    Queue.prototype.EnQueue = function (vElement)  {
//        if (arguments.length == 0)
//            return -1; //元素入队
//        for (var i = 0; i < arguments.length; i++) {
//            aElement.push(arguments[i]);
//        }
//        return aElement.length;
//    }
//    /*
//    * @brief: 元素出队
//    * @return: vElement
//    * @remark: 当队列元素为空时,返回null
//    */
//    Queue.prototype.DeQueue = function ()  {
//        if (aElement.length == 0)
//            return null;
//        else
//            return aElement.shift();
//    }
//    /*
//    * @brief: 获取队列元素个数
//    * @return: 元素个数
//    */
//    Queue.prototype.GetSize = function () {
//        return aElement.length;
//    }
//    /*
//    * @brief: 返回队头素值
//    * @return: vElement
//    * @remark: 若队列为空则返回null
//    */
//    Queue.prototype.GetHead = function ()  {
//        if (aElement.length == 0)
//            return null;
//        else
//            return aElement[0];
//    }
//    /*
//    * @brief: 返回队尾素值
//    * @return: vElement
//    * @remark: 若队列为空则返回null
//    */
//    Queue.prototype.GetEnd = function ()  {
//        if (aElement.length == 0)
//            return null;
//        else
//            return aElement[aElement.length - 1];
//    }
//    /*
//    * @brief: 将队列置空
//    */
//    Queue.prototype.MakeEmpty = function()  {
//        aElement.length = 0;
//    }
//    /*
//    * @brief: 判断队列是否为空
//    * @return: 队列为空返回true,否则返回false
//    */
//    Queue.prototype.IsEmpty = function ()  {
//        if (aElement.length == 0)
//            return true;
//        else
//            return false;
//    }
//    /*
//    * @brief: 将队列元素转化为字符串
//    * @return: 队列元素字符串
//    */
//    Queue.prototype.toString = function ()  {
//        var sResult = (aElement.reverse()).toString();
//        aElement.reverse()
//        return sResult;
//    }
//    /*
//   *@brief:根据队列唯一ID来修改该队列元素信息
//   *@param: id(队列元素的唯一标识)
//   *@return:返回队列修改后的元素
//   */
//    Queue.prototype.FindIndex = function (id) {
//        for (var i = 0; i < aElement.length; i++)
//        {
//            if (aElement[i].length != 0 && aElement[i].ID.length != 0)
//            {
//                if (aElement[i].ID == id)
//                {
//                    return i;
//                }
//            }
//        }
//        return -1;
//    }
//    /*
//      *@brief:根据队列唯一ID来修改该队列元素信息
//      *@param: id(队列元素的唯一标识)
//      *@return:返回队列修改后的元素
//      */
//    Queue.prototype.Find= function (id) {
//        for (var i = 0; i < aElement.length; i++) {
//            if (aElement[i].length != 0 && aElement[i].ID.length != 0) {
//                if (aElement[i].ID == id) {
//                    return aElement[i];
//                }
//            }
//        }
//        return null;
//    }
//    /*
//    *@brief:根据队列唯一ID来修改该队列元素信息
//    *@param: vElement元素(必须包含ID唯一属性)
//    *@return:返回队列修改后的元素
//    */
//    Queue.prototype.PatchQueue = function (vElement) {
//        if (vElement.length == 0 || vElement.ID.length)
//            return null;
//        else {
//            var Index = this.FindIndex(vElement.ID);
//            aElement[Index] = vElement;
//            return aElement[Index];
//        }
//    }
//}