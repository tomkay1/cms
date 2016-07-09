var version = "1.1.2";
seajs.config({
    alias : {
        'jquery'     : '/assets/libs/jquery-2.2.3.min.js',
        // 'jquery-ui'  :   '../assets/libs/jquery-ui.min.js',
        'bootstrap'  :   '/assets/libs/bootstrap.min.js',
        'layer'      :   '/assets/libs/layer/layer.js'
    },
    preload : ['jquery']
});
