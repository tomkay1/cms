define(function(require, exports, module) {
    exports.init = function () {
        $('.domain a').on('click', function (e) {
            var _href = $(this).attr('href');
            $('#content iframe', window.parent.document).attr('src', _href);
            e.preventDefault();
        });

        $(document.body).find('.website-items').on('mouseenter', function () {
            $(this).children('.website-preview').stop().fadeIn();
        }).on('mouseleave', function () {
            $(this).children('.website-preview').stop().fadeOut();
        });

        $(document.body).find('.inner-box button').on('click', function () {
            var _parent = $(this).parent();
            _parent.addClass('active').siblings().removeClass('active');
        });
    };
});
