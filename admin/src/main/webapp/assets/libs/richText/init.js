KindEditor.ready(function (K) {
    window.editor = K.create('#content', {
        resizeType: '0',
        width: '1200px',
        height: '300px',
        uploadJson: '/cms/kindeditorUpload',
        afterBlur: function () { this.sync(); }
    });
});
