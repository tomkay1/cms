package com.huotu.hotcms.admin.controller;

import com.huotu.hotcms.admin.util.web.CookieUser;
import com.huotu.hotcms.service.common.ArticleSource;
import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.model.ArticleCategory;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.service.ArticleService;
import com.huotu.hotcms.service.util.PageData;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by chendeyu on 2016/1/6.
 */
@Controller
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    //
//    @Autowired
//    private HostService hostService;
//
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CookieUser cookieUser;

    @RequestMapping("/articleList")
    public ModelAndView articleList(HttpServletRequest request) throws Exception
    {
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/contents/articleList.html");

        return  modelAndView;
    }





    @RequestMapping(value = "/addArticle")
    public ModelAndView addArticle(HttpServletRequest request,Integer customerid) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/contents/addArticle.html");
        Set<Category> categorys=categoryRepository.findByCustomerId(customerid);
//        List<Region> regions =regionRepository.findAll();
        modelAndView.addObject("categorys",categorys);
        return  modelAndView;
    }

    /*
  * 修改栏目
  * */
    @RequestMapping("/updateArticle")
    public ModelAndView updateArticle(@RequestParam(value = "id",defaultValue = "0") Long id,Integer customerId) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/contents/updateArticle.html");
        Article article= articleService.findById(id);
        Set<Category> categorys=categoryRepository.findByCustomerId(customerId);
        modelAndView.addObject("categorys",categorys);
        modelAndView.addObject("article",article);
        return modelAndView;
    }


    /*
      * 更新公告
      * */
    @RequestMapping(value = "/saveArticle",method = RequestMethod.POST)
    @Transactional(value = "transactionManager")
    @ResponseBody
    public ResultView saveArticle(Article article,Long categoryId,int articleSourceId){
        ResultView result=null;
        try {
            Long id = article.getId();
            Category category = categoryRepository.getOne(categoryId);
            ArticleSource articleSource=ArticleSource.valueOf(articleSourceId);
            if(id!=null)
            {
                Article articleOld = articleService.findById(article.getId());
                article.setCreateTime(articleOld.getCreateTime());
                article.setUpdateTime(LocalDateTime.now());
                article.setLauds(articleOld.getLauds());
                article.setScans(articleOld.getScans());
                article.setUnlauds(articleOld.getUnlauds());
            }
            else{
                article.setCreateTime(LocalDateTime.now());
                article.setUpdateTime(LocalDateTime.now());
            }
            article.setArticleSource(articleSource);
            article.setCategory(category);
            articleService.saveArticle(article);
            result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
        }
        catch (Exception ex)
        {
            result=new ResultView(ResultOptionEnum.FAILE.getCode(),ResultOptionEnum.FAILE.getValue(),null);
        }
        return  result;
    }


    @RequestMapping(value = "/getArticleList")
    @ResponseBody
    public PageData<ArticleCategory> getArticleList(@RequestParam(name="customerId",required = false) Integer customerId,
                                                  @RequestParam(name="title",required = false) String title,
                                                  @RequestParam(name = "page",required = true,defaultValue = "1") int page,
                                                  @RequestParam(name = "pagesize",required = true,defaultValue = "20") int pageSize){
        PageData<ArticleCategory> pageModel=articleService.getPage(customerId,title, page, pageSize);
        return pageModel;
    }

    @RequestMapping(value = "/deleteArticle",method = RequestMethod.POST)
    @ResponseBody
    public ResultView deleteArticle(@RequestParam(name = "id",required = true,defaultValue = "0") Long id,int customerId,HttpServletRequest request) {
        ResultView result=null;
        try{
            if(cookieUser.isSupper(request)) {
                Article article = articleService.findById(id);
                article.setDeleted(true);
                articleService.saveArticle(article);
                result=new ResultView(ResultOptionEnum.OK.getCode(),ResultOptionEnum.OK.getValue(),null);
            }
            else {
                result=new ResultView(ResultOptionEnum.NO_LIMITS.getCode(),ResultOptionEnum.NO_LIMITS.getValue(),null);
            }
        }
        catch (Exception ex)
        {
            result=new ResultView(ResultOptionEnum.FAILE.getCode(),ResultOptionEnum.FAILE.getValue(),null);
        }
        return  result;
    }


}