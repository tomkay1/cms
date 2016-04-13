var version="1.0"
seajs.config({
	alias: {
		"jquery": "js/jquery-1.9.1.min.js?v="+version,
		"bootstrap": "js/bootstrap.min.js?v="+version,
		"common":"js/page/common.js?t=552225?v="+version,
		"layer":"libs/layer/layer.js?v="+version,
		"main":"js/main.js?v="+version
	},
	preload: ['jquery']
});