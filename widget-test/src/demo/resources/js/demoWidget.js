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
                // this.ps = widgetProperties();
                var me = this;
                me.randomValue = Math.random();
                console.error('our random value:', me.randomValue, me.properties);
                $('#DataFetcher').bind('click', function () {
                    if (CMSDebugMode)
                        console.error('some one click data fetch');
                    getDataSource('findLink', 123, function (data) {
                        if (CMSDebugMode)
                            console.error('findLink:', data);
                        me.properties.DataFetcherResult = data;
                        console.error('DataFetcherResult updated', me.properties);
                    }, function (jhr, status, error) {
                        if (CMSDebugMode)
                            console.error('findLink error,', status, error);
                    })
                });
            },
            close: function (globalId) {
                console.error('our random value:', this.randomValue);
                $('#DataFetcher').unbind();
            },
            saveComponent: function (onSuccess, onFailed) {
                var that = this;
                console.error('our random value:', that.randomValue, that.properties);
                $(":text").each(function () {
                    var name = $(this).attr("name");
                    that.properties[name] = $(this).val();

                });
                // onSuccess(this.ps);
                // return this.ps;
            }
        },
        // 浏览相关 暂无
        browse: {}
    }
);


