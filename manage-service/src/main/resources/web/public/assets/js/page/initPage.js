define(function(require, exports, module) {
    exports.init = function () {
        var clipboard = new Clipboard('.clipBtn');

        clipboard.on('success', function(e) {
            
        });

        clipboard.on('error', function(e) {

        });
    };
});
