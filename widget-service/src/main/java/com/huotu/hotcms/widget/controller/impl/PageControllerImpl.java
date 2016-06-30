///*
// * 版权所有:杭州火图科技有限公司
// * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
// *
// * (c) Copyright Hangzhou Hot Technology Co., Ltd.
// * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
// * 2013-2016. All rights reserved.
// */
//
//package com.huotu.hotcms.widget.controller.impl;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.common.io.CharStreams;
//import com.huotu.hotcms.widget.controller.PageController;
//import com.huotu.hotcms.widget.page.Page;
//import com.huotu.hotcms.widget.service.PageService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.net.URISyntaxException;
//import java.util.List;
//
///**
// * Created by hzbc on 2016/5/27.
// */
//@Controller
//public class PageControllerImpl implements PageController {
//
//    @Autowired
//    private PageService pageService;
//
//
//
//    @Override
//    public List<Page> getPageList(long ownerId) throws IOException {
//        return null;
//        //return pageService.getPageFromXMLConfig(ownerId,pageId);
//    }
//
//
//    @Override
//    public Page getPage(String pageId) throws IOException {
//        return pageService.getPageFromXMLConfig(pageId);
//    }
//
//
//    @Override
//    public void savePage(@PathVariable String pageId,HttpServletRequest request)
//            throws IOException, URISyntaxException {
//        String pageJson=CharStreams.toString(request.getReader());
//        ObjectMapper objectMapper=new ObjectMapper();
//        Page page=objectMapper.readValue(pageJson, Page.class);
//        pageService.parsePageToXMlAndSave(page, pageId);
//    }
//
//
//    @Override
//    public void addPage(@PathVariable long ownerId,HttpServletRequest request) throws IOException {
//        String pageJson=CharStreams.toString(request.getReader());
//        ObjectMapper objectMapper=new ObjectMapper();
//        Page page=objectMapper.readValue(pageJson, Page.class);
//    }
//
//
//    @Override
//    public void deletePage(@PathVariable String pageId,@RequestParam long ownerId) throws IOException {
//        pageService.deletePage(ownerId, pageId);
//    }
//
//
//    @Override
//    public void savePagePartProperties(@PathVariable String pageId,@PathVariable String propertyName) {
//
//    }
//
//
//    @Override
//    public String startEdit() {
//        return "/edit/edit.html";
//    }
//}
