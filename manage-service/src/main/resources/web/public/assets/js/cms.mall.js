/**
 * Created by Neo on 2016/10/11.
 */
// 组件的一些交互
$(function () {
    var topNavbar = $('.topnavbar-new')
    if (!navigator.userAgent.match(/mobile/i)) {
        topNavbar.find('.drop-down').mouseenter(function () {
            $(this).addClass('drop-down-on');
        }).mouseleave(function () {
            $(this).removeClass('drop-down-on');
        });
        $(window).resize(function () {
            var topnavbarBody = $('.topnavbar-body');
            if( topnavbarBody.is(':hidden') && $(window).width() > 991) topnavbarBody.show();
        });
    }

    topNavbar.find('.drop-down').click(function () {
        $(this).toggleClass('drop-down-on');
    });

    $('.menu-toggle-icon').click(function(event) {
        event.preventDefault();
        $(this).toggleClass('on');
        $('.topnavbar-body').slideToggle();
    });

    $('.copyright-3').find('.list-unstyled').children('li').click(function () {
        if(navigator.userAgent.match(/mobile/i)) {
            $(this).find('.sub-list').slideToggle();
        }
    });

    $('.image-text-list-gray').find('img').mouseenter(function () {
        $(this).toggleClass('filter-gray');
    }).mouseleave(function () {
        $(this).toggleClass('filter-gray');
    });

    // 文章点击显示
    $('.information-list-more').click(function() {
        var ele = $(this).parent('.information-list-left-header').siblings('.information-list-left-body');
        ele.stop().slideToggle('fast');
    });
});


// 商城的一些交互
$(function () {
    $('.mall-product-lists').find('.tab-item').children('a').mouseover(function () {
        var parent = $(this).parent();
        parent.addClass('tab-selected').siblings('li').removeClass('tab-selected');
    });

    $('.mall-main-swiper').swiper({
        pagination: '.swiper-pagination',
        nextButton: '.swiper-button-next',
        prevButton: '.swiper-button-prev',
        autoplay: 5000,
        slidesPerView: 1,
        paginationClickable: true,
        observer: true,
        observeParents: true,
        updateOnImagesReady: true,
        loop: true
    });

    $('.recommend-list').find('.swiper-container').swiper({
        nextButton: '.swiper-button-next',
        prevButton: '.swiper-button-prev',
        slidesPerView: 4,
        slidesPerGroup : 4,
        loop : true,
        speed: 1000,
        observer: true,
        observeParents: true,
        updateOnImagesReady : true
    });

    // 添加菜单交互

    $('.categorys-dorpdown').find('.item').mouseenter(function () {
        $(this).addClass('show');
    }).mouseleave(function () {
        $(this).removeClass('show');
    });

    $('.mall-shortcut').find('.dorpdown').mouseenter(function () {
        $(this).find('.content-list').show();
    }).mouseleave(function () {
        $(this).find('.content-list').hide();
    });
});

// 商城注册的表单验证
jQuery.validator.addMethod("isPhone", function(value, element) {
    var tel = /^1[34578]\d{9}$/;
    return this.optional(element) || (tel.test(value));
}, "请正确填写您的手机号");

jQuery.validator.addMethod("validPassword", function(value, element) {
    var tel = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$/;
    return this.optional(element) || (tel.test(value));
}, "请正确填写密码");

$(".mall-registerForm").validate({
    rules: {
        userPhone: {
            required: true,
            isPhone: true
        },
        password: {
            required: true,
            minlength: 6,
            maxlength: 16,
            validPassword: true
        },
        confirmPassword: {
            required: true,
            equalTo: "#js-registerPassword"
        }
    },
    messages: {
        userPhone: {
            required: "请输入你的手机号"
        },
        password:  {
            required: "请输入你的密码",
            minlength: "密码长度不小于6位",
            maxlength: "密码长度不大于16位"
        },
        confirmPassword: {
            required: "请输入密码",
            equalTo: "两次密码输入不一致"
        }
    },
    submitHandler: function(form) {
        form.submit();
    },
    focusCleanup: true
});