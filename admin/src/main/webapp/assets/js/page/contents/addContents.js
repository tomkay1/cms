/**
 * Created by chendeyu on 2016/1/11.
 */
define(function (require, exports, module) {

    $("#categoryId").on("change",function(){
        var modelId= $("#categoryId").find("option:selected").attr("data-modelType");
        var commonUtil = require("common");
        commonUtil.setDisabled("jq-cms-Save");
        var customerId =commonUtil.getQuery("customerId");
        if(modelId==0){
                $.get("/article/addArticle?customerId="+customerId, function (html) {
                    $("#widget").html(html);
                });}
        if(modelId==1){
                $.get("/notice/addNotice?customerId="+customerId, function (html) {
                    $("#widget").html(html);
                });}
        if(modelId==2){
                $.get("/video/addVideo?customerId="+customerId, function (html) {
                    $("#widget").html(html);
                });}
        if(modelId==3){
                $.get("/gallery/addGallery?customerId="+customerId, function (html) {
                    $("#widget").html(html);
                });}
        if(modelId==4) {
            $.get("/download/addDownload?customerId="+customerId, function (html) {
                $("#widget").html(html);
            });}
        if(modelId==5){
            $.get("/link/addLink?customerId="+customerId, function (html) {
                    $("#widget").html(html);
                });
        }
        else{

        }
        //$.get("/article/addArticle", function (html) {
        //    $("#widget").html(html);
        //});
    })
});