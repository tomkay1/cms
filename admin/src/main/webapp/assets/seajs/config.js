seajs.config({
	alias: {
		"jquery": "js/jquery-1.9.1.min.js",
		"bootstrap": "js/bootstrap.min.js",
		"validate": "libs/validate/jquery.validate.min.js",
		"message": "libs/validate/jquery.validate.addMethod.js",
		"common":"js/page/common.js",
		"main":"js/page/main.js"
	},
	preload: ['jquery']
});