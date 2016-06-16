var app = (function(){
    var Model = function(value){
        this._v = value;
        this._listeners = [];
    }
    Model.prototype.set = function(value){
        var self = this;
        self._v = value;
        setTimeout(function(){
            self._listeners.forEach(function(listener){
                listener.call(this,value);
            })
        })
    }
    Model.prototype.watch = function(func){
        this._listeners.push(func);
    }
    Model.prototype.bind = function(node){
        var self = this;
        this.watch(function(value){
            if(node.tagName.toUpperCase()=="INPUT"  && !self.inputEvent){
                node.addEventListener("keyup",function(){
                    var _v = this.value;
                    if(_v != value){
                        self.set(_v);
                    }
                    self.inputEvent = 1;
                })
                node.value = value;
            }else{
                node.innerHTML = value;
            }
        })
    }
    function controller(controllername,callback){
        var models = {},
            search = typeof controllername=="string" ? "[controller=" + controllername + "]" : "[controller]",
            controller = document.querySelector(search),init = eval("("+controller.getAttribute("init")+")"),$scope = {};
        if(!controller) return;
        var views = Array.prototype.slice.call(controller.querySelectorAll("[bind]"),0);
        views.forEach(function(view){
            var modelname = view.getAttribute("bind");
            (models[modelname] = models[modelname] || new Model()).bind(view);
            $scope = createVisitors($scope,models[modelname],modelname);
        });
        for(var index in init){
            $scope[index] = init[index];
        }
        callback.call(this,$scope);
    }
    function createVisitors($scope,model,property){
        $scope.__defineSetter__(property,function(value){
            model.set(value);
        })
        return $scope;
    }
    return {
        controller: controller
    }
})();