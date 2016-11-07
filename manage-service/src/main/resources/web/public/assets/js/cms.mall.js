/**
 * Created by Neo on 2016/10/11.
 */

$('.product-lists').find('.tab-item').children('a').mouseover(function () {
    var parent = $(this).parent();
    parent.addClass('tab-selected').siblings('li').removeClass('tab-selected');
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
var categorysItem = $('.categorys-dorpdown').find('.item');

categorysItem.mouseenter(function () {
    $(this).find('.item-sub').addClass('show');
}).mouseleave(function () {
    $(this).find('.item-sub').removeClass('show');
});