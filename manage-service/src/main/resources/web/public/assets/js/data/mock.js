CMSWidgets.pushNextWidgetIdentity('com.huotu.hotcms.widget.picBanner-picBanner:1.0-SNAPSHOT');
/**
 * Created by admin on 2016/6/27.
 */

CMSWidgets.initWidget({
// 编辑器相关
    editor: {
        properties: null,
        saveComponent: function (onSuccess, onFailed) {
            this.properties.linkUrl = $(".picBUrl").val();
            if (this.properties.pcImg == "" && this.properties.mobileImg == "") {
                onFailed("组件数据缺少,未能保存,请完善。");
                return;
            }
            onSuccess(this.properties);
            return this.properties;
        },
        uploadImage: function () {
            var me = this;
            uploadForm({
                ui: '#picBannerMaxImg',
                inputName: 'file',
                maxWidth: 1920,
                maxHeight: 200,
                maxFileCount: 1,
                isCongruent: false,
                successCallback: function (files, data, xhr, pd) {
                    me.properties.pcImg = data.fileUri;
                },
                deleteCallback: function (resp, data, jqXHR) {
                    me.properties.pcImg = "";
                }
            });
            uploadForm({
                ui: '#picBannerMinImg',
                inputName: 'file',
                maxWidth: 1200,
                maxHeight: 200,
                maxFileCount: 1,
                isCongruent: false,
                successCallback: function (files, data, xhr, pd) {
                    me.properties.mobileImg = data.fileUri;
                },
                deleteCallback: function (resp, data, jqXHR) {
                    me.properties.mobileImg = "";
                }
            });
        },
        initProperties: function () {
            this.properties.pcImg = "";
            this.properties.mobileImg = "";
            this.properties.linkUrl = "";
        },
        open: function (globalId) {
            this.properties = widgetProperties(globalId);
            this.initProperties();
            this.uploadImage();
        },
        close: function (globalId) {
            $('#picBannerMaxImg').siblings().remove();
            $('#picBannerMinImg').siblings().remove();
        }
    }
});
