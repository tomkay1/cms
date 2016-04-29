(function($) {
	"use strict";

	var methods;
	$.extend({
		toolbar: function(options) {
			var settings, ops, personQrCode, subscribeCode;
			/**
			 * [settings 方法初始化参数]
			 * @type {Object}
			 * root: 项目路径，外部传入
			 * parentElement: 插入父级容器，因为绝对定位，所以使用 body
			 */
			settings = {
				root: '',
				topElement: '.hottech_top_anchor',
				parentElement: 'body',
				personQrElement: '.hottech_wechat_code',
				subscribeElement: '.hottech_share_code',
			};
			ops = $.extend(settings, options);
			/**
			 * @type {Object}
			 * codeElement: 二维码生成的元素容器
			 * codeURL： 后台生成API地址
			 */
			personQrCode = {
				codeElement: ops.personQrElement,
				codeURL: ops.root + '/bind/personCode'
			};
			subscribeCode = {
				codeElement: ops.subscribeElement,
				codeURL: ops.root + '/bind/subscribeCode'
			};

			methods.createDOM(ops.parentElement);
			methods.createTwoDimensionCode(personQrCode, subscribeCode);
			methods.goToAnchor(ops.topElement);
		}
	});

	methods = {
		/**
		 * [createDOM 生成侧边工具栏代码]
		 * @param  {[type]} parentElement [父级容器，一般是 body]
		 * @return {[type]}               [description]
		 */
		createDOM: function(parentElement) {
			var parent = $(parentElement),
				div = $('<div id="tool-bar"></div>'),
				container = [];

			container.push('<ul>');
			container.push('<li id="hottech_wechat">');
			container.push('<div class="hottech_wechat_code"></div>');
			container.push('</li>');
			container.push('<li id="hottech_share">');
			container.push('<div class="hottech_share_code"></div>');
			container.push('</li>');
			container.push('<li id="hottech_top">');
			container.push('<a class="hottech_top_anchor"></a>');
			container.push('</li>');
			container.push('</ul>');

			div.html(container.join(""));
			parent.append(div);

			methods.showCode('#hottech_wechat', '.hottech_wechat_code');
			methods.showCode('#hottech_share', '.hottech_share_code');
		},
		/**
		 * [createTwoDimensionCode 把生成的二维码作为背景图片添加给容器]
		 * @return {[type]} [description]
		 */
		createTwoDimensionCode: function() {
			$.each(arguments, function(v, i) {
				$(i.codeElement).css('background-image', "url(" + i.codeURL + ")");
			});
		},
		/**
		 * [goToAnchor 回滚到顶部]
		 * @param  {[type]} clickElment [触发元素]
		 * @return {[type]}             [阻止默认行为]
		 */
		goToAnchor: function(clickElment) {
			$(clickElment).on('click', function() {
				$("html, body").animate({
					scrollTop: 0
				}, 300);
				return false;
			});
		},
		
		showCode: function (parent, child) {
			$(parent).mouseenter(function () {
				$(child).show();
			});
			$(parent).mouseleave(function () {
				$(child).hide();
			});
		}

	};

})(jQuery);