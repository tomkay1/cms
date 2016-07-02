 var main = {
        test: function () {
            console.log('测试');
        },
        pageTabs: function () {
            $('#content-tabs a, #product-tabs a').on('click',function (e) {
                e.preventDefault();
                $(this).tab('show')
            });
        },
        init: function() {
            main.test();
            main.pageTabs();
        }
    };
    main.init();
