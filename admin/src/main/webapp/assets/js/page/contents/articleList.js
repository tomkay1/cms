/**
 * Created by chendeyu on 2015/12/24.
 */
define(function (require, exports, module) {
    $("#jq-cms-return").click(function(){
        var layer=require("layer");
        var layerIndex = parent.layer.getFrameIndex(window.name); //获取窗口索引
        parent.layer.close(layerIndex);
    })

});