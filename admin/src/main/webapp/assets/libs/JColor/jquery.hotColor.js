var jHotColor={
    initColor:function(callback){
        var obj = $(".js-hot-color");
        $.each(obj, function (item, dom) {
            var inputValueName = $(dom).data("input");
            var defaultColor = $(dom).data("default");
            $(dom).spectrum({
                allowEmpty: true,
                chooseText: "ȷ��",
                cancelText: "ȡ��",
                color: defaultColor,
                preferredFormat: "rgb",
                showInitial: false,
                showButtons: false,
                showInput: true,
                width: "120",
                change: function (color) {
                    if (callback != undefined && typeof callback === "function") {
                        $("input[name='" + inputValueName + "']").val(color.toHexString());
                        callback(color.toHexString());
                    }
                    else {
                        $("input[name='" + inputValueName + "']").val(color.toHexString());
                    }
                }
            });
        });
    }
}
function hotColor(callback) {
    var obj = $(".js-hot-color");
    $.each(obj, function (item, dom) {
        var inputValueName = $(dom).data("input");
        var defaultColor = $(dom).data("default");
        $(dom).spectrum({
            allowEmpty: true,
            chooseText: "确定",
            cancelText: "取消",
            color: defaultColor,
            preferredFormat: "rgb",
            showInitial: false,
            showButtons: true,
            showInput: true,
            width: "120",
            change: function (color) {
                if (callback != undefined && typeof callback === "function") {
                    $("input[name='" + inputValueName + "']").val(color.toHexString());
                    callback(color.toHexString());
                }
                else {
                    $("input[name='" + inputValueName + "']").val(color.toHexString());
                }
            }
        });
    });
}
