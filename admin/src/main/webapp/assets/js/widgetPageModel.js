define(function (require, exports, module) {
    exports.widgetPage=function(){
        var $this=this;
        return{
            getInstance:function(){
              return $this;
            },
            setName:function(name){
                $this.pageName=name;
                return $this;
            },
            setKeyWords:function(keyWords){
                window.console.log($this.pageKeyWords);
                $this.pageKeyWords=keyWords;
                return $this;
            },
            setDescription:function(description){
                $this.pageDescription=description;
                return $this;
            },
            setUrl:function(url){
                $this.url=url;
                return $this;
            },
            setBackGround:function(backGround){
                $this.pageBackGround=backGround;
                return $this;
            },
            setPageBackImage:function(backImage){
                $this.pageBackImage=backImage;
                return $this;
            },
            setPageBackRepeat:function(backRepect){
                $this.pageBackRepeat=backRepect;
                return $this;
            },
            setPageBackAlign:function(backAlign){
                $this.pageBackAlign=backAlign;
                return $this;
            },
            setPageBackVertical:function(backVertical){
                $this.pageBackVertical=backVertical;
                return $this;
            },
            setWidgetLayout:function(widgetLayout){
                $this.layout=widgetLayout;
                return $this;
            },
            pushWidgetLayout:function(widgetLayout){
                $this.layout.push(widgetLayout);
                return $this;
            },
            setModel:function(widgetPage){
                for(var item in widgetPage){
                    $this[item]=widgetPage[item];
                }
                return $this;
            }
        };
    }
    exports.widgetLayout=function(){
        var $this=this;
        return{
            getInstance:function(){
                return $this;
            },
            setLayoutType:function(layoutType){
                $this.layoutType=layoutType;
                return $this;
            },
            setWidgetModule:function(widgetModule){
                $this.module=widgetModule;
                return $this;
            },
            pushWidgetModule:function(widgetModule){
                $this.module.push(widgetModule);
                return $this;
            },
            setModel:function(widgetLayout){
                for(var item in widgetLayout){
                    $this[item]=widgetLayout[item];
                }
                return $this;
            }
        }
    }
    exports.widgetModule=function(){
        var $this=this;
        return{
            getInstance:function(){
                return $this;
            },
            setPosition:function(position){
                $this.position=position;
                return $this;
            },
            setWidgetBase:function(widgetBase){
                $this.widget=widgetBase;
                return $this;
            },
            pushWidgetBase:function(widgetBase){
                $this.widget.push(widgetBase);
                return $this;
            },
            setModel:function(widgetModule){
                for(var item in widgetModule){
                    $this[item]=widgetModule[item];
                }
                return $this;
            }
        }
    }
    exports.widgetBase=function(){
        var $this=this;
        return{
            getInstance:function(){
                return $this;
            },
            setId:function(id){
                $this.id=id;
                return $this;
            },
            setWidgetUri:function(widgetUri){
                $this.widgetUri=widgetUri;
                return $this;
            },
            setWidgetProperty:function(widgetProperty){
                $this.property=widgetProperty;
                return $this;
            },
            pushProperty:function(widgetProperty){
                $this.property.push(widgetProperty);
                return $this;
            },
            setModel:function(widgetBase){
                for(var item in widgetBase){
                    $this[item]=widgetBase[item];
                }
                return $this;
            }
        }
    }

    /**
     * @brief: 表单系列化Json对象
     * @return: 返回系列化后的json对象
     */
    $.fn.serializeJson = function () {
        var serializeObj = {};
        var array = this.serializeArray();
        var str = this.serialize();
        $(array).each(function () {
            if (serializeObj[this.name]) {
                if ($.isArray(serializeObj[this.name])) {
                    serializeObj[this.name].push(this.value);
                } else {
                    serializeObj[this.name] = [serializeObj[this.name], this.value];
                }
            } else {
                serializeObj[this.name] = this.value;
            }
        });
        return serializeObj;
    };
});