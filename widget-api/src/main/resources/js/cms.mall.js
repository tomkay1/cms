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

