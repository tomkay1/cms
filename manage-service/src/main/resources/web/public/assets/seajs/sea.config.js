var version = "1.1.2";
seajs.config({
    alias : {
        'jquery'     : '/assets/libs/jquery-2.2.3.min.js',
        // 'jquery-ui'  :   '../assets/libs/jquery-ui.min.js',
        'bootstrap'  :   '/assets/libs/bootstrap.min.js',
        // 'validator'  :   '../assets/libs/bootstrapValidator.min.js',
        // 'table'      :   '../assets/libs/bootstrap-table.min.js',
        // 'tableCN'    :   '../assets/libs/bootstrap-table-zh-CN.min.js',
        // 'clipboard'  :   '../assets/libs/clipboard.min.js',
        // 'htmlClean'  :   '../assets/libs/jquery.htmlClean.js',
        // 'slick'      :   '../assets/libs/slick.min.js',
        'layer'      :   '/assets/libs/layer/layer.js',
        'main'       :   '/assets/js/main.js?v=' + version,
        'mainPJ'     :   '/assets/js/page/mainPage.js?v=' + version,
        'initPJ'     :   '/assets/js/page/initPage.js?v=' + version,
        'framePJ'    :   '../assets/js/page/framePage.js?v=' + version,
        'dataPJ'     :   '../assets/js/page/dataPage.js?v=' + version,
        'modelPJ'    :   '../assets/js/page/modelPage.js?v=' + version,
        'editPJ'     :   '../assets/js/page/editPage.js?v=' + version,
        'confMJ'     :   '../assets/js/module/confModule.js?v=' + version,
        'dataHandle' :   '../assets/js/data/dataHandle.js?v=' + version

    },
    preload : ['jquery']
});
