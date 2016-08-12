/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

CMSWidgets.initWidget(
    {
        // 编辑器相关
        editor: {
            open: function (globalId) {
                if (CMSDebugMode)
                    console.error('初始化编辑器', this);
                window['inited'] = true;
                this.ps = widgetProperties();
                var me = this;
                $('#DataFetcher').bind('click', function () {
                    if (CMSDebugMode)
                        console.error('some one click data fetch');
                    getDataSource('findLink', 123, function (data) {
                        if (CMSDebugMode)
                            console.error('findLink:', data);
                        me.ps.DataFetcherResult = data;
                    }, function (jhr, status, error) {
                        if (CMSDebugMode)
                            console.error('findLink error,', status, error);
                    })
                });
            },
            close: function (globalId) {
                $('#DataFetcher').unbind();
            },
            saveComponent: function (onSuccess, onFailed) {
                var that = this;
                $(":text").each(function () {
                    var name = $(this).attr("name");
                    that.ps[name] = $(this).val();

                });
                onSuccess(this.ps);
                return this.ps;
            }
        },
        // 浏览相关 暂无
        browse: {}
    }
);


