//
function b() {
	h = $(window).height(),
	t = $(document).scrollTop(),
	t > h ? $("#moquu_top").show() : $("#moquu_top").hide()
}
function  personQrCode(){//商户公众号二维码
	var strwrite = rootUrl+"/bind/personCode";
	$('.hottech_wxinh').css('background-image',"url(" + strwrite + ")");
}

function  subscribeCode(){//商户公众号二维码
	var strwrite = rootUrl+"/bind/subscribeCode";
	$('.hottech_wshareh').css('background-image',"url(" + strwrite + ")");
}
$(document).ready(function() {
	b(),
	$("#moquu_top").click(function() {
		$(document).scrollTop(0)
	})
}),
$(window).scroll(function() {
	b()
});
