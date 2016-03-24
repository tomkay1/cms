var navigate= {
    topCategoryArtive: function (index, div, element) {
        $($("#" + div).find(element).get(index)).addClass("active");
    },
    CategoryActive:function(index, div, element,active){
        $($("#" + div).find(element).get(index)).addClass(active);
    }
}