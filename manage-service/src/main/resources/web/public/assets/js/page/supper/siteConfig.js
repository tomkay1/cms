/**
 * Created by chendeyu on 2015/12/23.
 */
define(function (require, exports, module) {
    var commonUtil = require("common");
    var ownerId = commonUtil.getQuery("ownerId");
    var siteId=commonUtil.getQuery("siteId");
    $("#siteConfigForm").validate({
        rules: {
            mobileDomain: {
                required: true,
            }
        },
        messages: {
            mobileDomain: {
                required: "微官网域名为必填项"
            }
        },
        submitHandler: function (form, ev) {
            var commonUtil = require("common");
            commonUtil.setDisabled("jq-cms-Save");
            $.ajax({
                url: "/manage/supper/saveConfig",
                data: {
                    id:$("#id").val(),
                    siteId: siteId,
                    ownerId: ownerId,
                    enabledMobileSite:$("#enabledMobileSite").val(),
                    mobileDomain:$("#mobileDomain").val()
                },
                type: "POST",
                dataType: 'json',
                success: function (data) {
                    var layer = require("layer");
                    if (data != null) {
                        var index = parseInt(data.code);
                        var layerIndex = parent.layer.getFrameIndex(window.name); //获取窗口索引
                        if (index == 200) {
                            layer.msg("保存成功，2秒后将会关闭", {time: 2000})
                            parent.layer.close(layerIndex);
                        }
                        if (index == 502) {
                            layer.msg("操作失败", {time: 2000})
                        }
                    }
                    commonUtil.cancelDisabled("jq-cms-Save");
                },
                error: function () {
                    commonUtil.cancelDisabled("jq-cms-Save");
                }
            })
            return false;
        },
        invalidHandler: function () {
            return true;
        },
    });
});



