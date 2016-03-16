package com.huotu.hotcms.admin.controller;

import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.model.widget.layoutTest;
import com.huotu.hotcms.service.widget.XmlTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

/**
 * 视图控制器
 * @since  1.0.0
 * @author xhl
 */
@Controller
@RequestMapping("/main")
public class MainController {

    private TemplateEngine templateEngine = new TemplateEngine();

    @Autowired
    private XmlTestService xmlTestService;
    @RequestMapping({"/index",""})
    public ModelAndView index(HttpServletRequest request, @RequestParam("customerid") Integer customerid) throws Exception{
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/view/main.html");
//        xmlTestService.testXmlJAXB();
//        xmlTestService.testJacksonXML();
        return modelAndView;
    }

    @RequestMapping( value = "/decorated")
    public ModelAndView decorated( @RequestParam("customerid") Integer customerid) throws Exception{
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/view/decorated.html");
        return modelAndView;
    }

    @RequestMapping(value = "/test")
    public ModelAndView test(Writer writers) throws IOException {
        String templateResource="<div th:text=\"${test}\" ></div><div th:text=\"${test2}\"></div><div article:foreach=\"article\"><div th:text=\"article.name\"></div></div>";
        Map<String, Object> property=new HashMap<>();
        Set<Article> article=new HashSet<>();
        Article article1=new Article();
        article1.setTitle("测试1");
        Article article2=new Article();
        article2.setTitle("测试1");
        article.add(article2);
        property.put("test","测试哦");
        property.put("article",article);
        Context context = new Context(Locale.CHINA,property);
        StringWriter writer = new StringWriter();
        templateEngine.process(templateResource, context, writer);
        System.out.print(writer.toString());
        ModelAndView modelAndView = new ModelAndView();

        Set<layoutTest> layoutTests=new HashSet<>();
        layoutTests.add(new layoutTest("<div style='height:30px; width:100%;border:1px #cccccc solid' th:text=\"${test}\"></div>"));
        layoutTests.add(new layoutTest("<div style='height:50px; width:100%;border:1px #cccccc solid'></div>"));
        layoutTests.add(new layoutTest("<div style='height:60px; width:100%;border:1px #cccccc solid'></div>"));
        layoutTests.add(new layoutTest("<div style='height:70px; width:100%;border:1px #cccccc solid'></div>"));
        layoutTests.add(new layoutTest("<div style='height:80px; width:100%;border:1px #cccccc solid'></div>"));
        layoutTests.add(new layoutTest("<div style='height:90px; width:100%;border:1px #cccccc solid'></div>"));
        modelAndView.addObject("layouts",layoutTests.toArray());
        modelAndView.addObject("test2","测试哦");
//        modelAndView.setView(new View);
        modelAndView.setViewName("/view/a_test.shtml");
//        writers.write("<div>测试哦</div>");
        return modelAndView;
    }
}
