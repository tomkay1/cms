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
        PutQueue:function (vElement)  {
            if (arguments==undefined && arguments.length == 0)
                return -1; //元素入队    
            for (var i = 0; i < arguments.length; i++) {
                var _index = JQueue.FindIndex(arguments[i].ID);
                if (_index==-1) {//不存在则新增
                    aElement.push(arguments[i]);
                }
                else {//存在则修改
                    JQueue.PatchQueue(arguments[i]);
                }
            }    
            return aElement.length;  
        },
        /**
        * @brief: 元素出队   
        * @return: vElement   
        * @remark: 当队列元素为空时,返回null   
        */   
        DeQueue:function ()  {    
            if (aElement != null && aElement.length == 0)
                return null;   
            else     
                return aElement.shift();  
        },
        /**
        * @brief: 获取队列元素个数   
        * @return: 元素个数   
        */   
        GetSize:function () {    
            return aElement.length; 
        },
        /**
        * @brief: 返回队头素值    
        * @return: vElement    
        * @remark: 若队列为空则返回null   
        */   
        GetHead:function ()  {    
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
        GetLast:function ()  {    
            if (aElement != null && aElement.length == 0)
                return null;   
            else     
                return aElement[aElement.length - 1];  
        },
        /**
        * @brief: 将队列置空     
        */   
        Empty:function()  {    
            aElement.length = 0;  
        },
        /**
        * @brief: 判断队列是否为空 
        * @return: 队列为空返回true,否则返回false   
        */   
        IsEmpty:function ()  {    
            if (aElement == null || aElement.length == 0)
                return true;   
            else     
                return false;  
        },
        /**
        * @brief: 将队列元素转化为字符串   
        * @return: 队列元素字符串   
        */   
        toString:function ()  {    
            var sResult = (aElement.reverse()).toString();   
            aElement.reverse()
            return sResult;
        },
        toJson: function () {
            return JSON.stringify(aElement);
        },
        /**
       *@brief:根据队列唯一ID来修改该队列元素信息
       *@param: id(队列元素的唯一标识)  
       *@return:返回队列修改后的元素
       */
        FindIndex:function (id) {
            for (var i = 0; i < aElement.length; i++)
            {
                if (aElement[i] != null && aElement[i].length != 0 && aElement[i].ID.length != 0)
                {
                    if (aElement[i].ID == id)
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
            for (var i = 0; i < aElement.length; i++) {
                if (aElement[i] != null && aElement[i].length != 0 && aElement[i].ID.length != 0) {
                    if (aElement[i].ID == id) {
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
            for (var i = 0; i < aElement.length; i++) {
                if (aElement[i] != null && aElement[i].length != 0 && aElement[i].ID.length != 0) {
                    if (aElement[i].ID == id) {
                        aElement.splice(i, 1);
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
                aElement[Index] = vElement;
                return aElement[Index];
            }
        }
     })
 })(jQuery);