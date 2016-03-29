define(function (require, exports, module) {
    var page={},layout={},module={},widget={};
    exports.widgetPage=function(){
        return{
            getInstance:function(){
              return page;
            },
            setName:function(name){
                $this.pageName=name;
                return page;
            },
            setKeyWords:function(keyWords){
                window.console.log($this.pageKeyWords);
                page.pageKeyWords=keyWords;
                return page;
            },
            setDescription:function(description){
                page.pageDescription=description;
                return page;
            },
            setUrl:function(url){
                page.url=url;
                return page;
            },
            setBackGround:function(backGround){
                page.pageBackGround=backGround;
                return page;
            },
            setPageBackImage:function(backImage){
                page.pageBackImage=backImage;
                return page;
            },
            setPageBackRepeat:function(backRepect){
                page.pageBackRepeat=backRepect;
                return page;
            },
            setPageBackAlign:function(backAlign){
                page.pageBackAlign=backAlign;
                return page;
            },
            setPageBackVertical:function(backVertical){
                page.pageBackVertical=backVertical;
                return page;
            },
            setWidgetLayout:function(widgetLayout){
                page.layout=[];
                page.layout.push(widgetLayout);
                return page;
            },
            pushWidgetLayout:function(widgetLayout){
                page.layout.push(widgetLayout);
                return page;
            },
            setModel:function(widgetPage){
                for(var item in widgetPage){
                    page[item]=widgetPage[item];
                }
                return page;
            }
        };
    }
    exports.widgetLayout=function(){
        return{
            getInstance:function(){
                layout={};
                return layout;
            },
            setLayoutType:function(layoutType){
                layout.layoutType=layoutType;
                return layout;
            },
            setWidgetModule:function(widgetModule){
                layout.module=widgetModule;
                return layout;
            },
            pushWidgetModule:function(widgetModule){
                layout.module.push(widgetModule);
                return layout;
            },
            setModel:function(widgetLayout){
                for(var item in widgetLayout){
                    layout[item]=widgetLayout[item];
                }
                return layout;
            }
        }
    }
    exports.widgetModule=function(){
        var $this=module;
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
        var $this=widget;
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