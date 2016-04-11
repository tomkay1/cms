define(function (require, exports, module) {
    exports.pageColor=function(callback) {
        require.async("spectrumColor", function () {
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
        })
    }
})