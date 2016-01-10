package com.huotu.hotcms.admin.controller;

import com.huotu.hotcms.admin.util.web.CookieUser;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.CategoryTreeModel;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.RouteService;
import com.huotu.hotcms.service.service.impl.SiteServiceImpl;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Created by chendeyu on 2015/12/31.
 */
@Controller
@RequestMapping("/category")
public class CategoryController {
    private static final Log log = LogFactory.getLog(CategoryController.class);

    @Autowired
    private CookieUser cookieUser;

    @Autowired
    CategoryService categoryService;

    @Autowired
    SiteRepository siteRepository;

    @Autowired
    SiteServiceImpl siteService;

    @Autowired
    RouteService routeService;

    /**
     * 栏目列表视图
     * */
    @RequestMapping("/categoryList")
    public ModelAndView categoryList(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/section/categoryList.html");
        return  modelAndView;
    }

    /**
     * 根据商户ID获得站点列表业务API xhl
     * */
    @RequestMapping("/getSiteList")
    @ResponseBody
    public ResultView getSiteList(@RequestParam(value = "customerId",defaultValue = "0") Integer customerid){
        ResultView result=null;
        try{
            Set<Site> sites=siteService.findByCustomerIdAndDeleted(customerid, false);
            if(sites!=null&&sites.size()>0) {
                result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), sites.toArray(new Site[sites.size()]));
            }else{
                result = new ResultView(ResultOptionEnum.NOFIND.getCode(), ResultOptionEnum.NOFIND.getValue(),null);
            }
        }
        catch (Exception ex){
            log.error(ex.getMessage());
            result=new ResultView(ResultOptionEnum.SERVERFAILE.getCode(),ResultOptionEnum.SERVERFAILE.getValue(),null);
        }
        return result;
    }

    /**
     * 获得栏目列表 xhl
     * */
    @RequestMapping("/getCategoryList")
    @ResponseBody
    public ResultView getCategotyList(@RequestParam(name="siteId",required = false) Long siteId,
                                      @RequestParam(name = "name",required=false) String name){
        ResultView resultView=null;
        try{
            Site site=siteService.getSite(siteId);
            List<Category> categoryList=categoryService.getCategoryBySiteAndDeleted(site,false);
            List<CategoryTreeModel> categoryTreeModelList= categoryService.ConvertCateGoryTreeByCategotry(categoryList);
            resultView = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(),categoryTreeModelList);
        }
        catch (Exception ex){
            log.error(ex.getMessage());
            resultView=new ResultView(ResultOptionEnum.SERVERFAILE.getCode(),ResultOptionEnum.SERVERFAILE.getValue(),null);
        }
        return resultView;
    }

    /*
   * 增加栏目
   * */
    @RequestMapping("/addCategory")
    public ModelAndView addModel(@RequestParam(value = "id", defaultValue = "0") Long id) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/section/addCategory.html");
        Category category =categoryService.getCategoryById(id);
        Site site=category.getSite();
        modelAndView.addObject("category",category);
        modelAndView.addObject("site",site);
        return modelAndView;
    }

    /*
   * 修改栏目
   * */
    @RequestMapping("/updateCategory")
    public ModelAndView updateCategory(@RequestParam(value = "id",defaultValue = "0") Long id) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/section/updateCategory.html");
        Category category =categoryService.getCategoryById(id);
        modelAndView.addObject("category",category);
        return modelAndView;
    }

    /**
     * 新增栏目信息 xhl
     * */
    @RequestMapping(value = "/saveCategory",method = RequestMethod.POST)
    @ResponseBody
    public ResultView saveCategory(Category category, Integer model,Long siteId,Long parentId,String rule,String template){
        ResultView result=null;
        try {
            if (!routeService.isPatterBySiteAndRule(category.getSite(), rule)) {
                Site site = siteRepository.findOne(siteId);
                Category categoryParent = categoryService.getCategoryById(parentId);
                category.setSite(site);
                category.setCustomerId(site.getCustomerId());
                if (model >= 0) {
                    category.setModelType(model);
                }
                category.setParent(categoryParent);
                category.setCreateTime(LocalDateTime.now());
                category.setUpdateTime(LocalDateTime.now());
                if (categoryService.saveCategoryAndRoute(category, rule, template)) {
                    result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
                } else {
                    result = new ResultView(ResultOptionEnum.FAILE.getCode(), ResultOptionEnum.FAILE.getValue(), null);
                }
            } else {
                result = new ResultView(ResultOptionEnum.ROUTE_EXISTS.getCode(), ResultOptionEnum.ROUTE_EXISTS.getValue(), null);
            }
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage());
            result=new ResultView(ResultOptionEnum.FAILE.getCode(),ResultOptionEnum.FAILE.getValue(),null);
        }
        return  result;
    }

    /**
     * 根据栏目ID来删除栏目 xhl
     * */
    @RequestMapping(value = "/deleteCategory",method = RequestMethod.POST)
    @ResponseBody
    public ResultView deleteCategory(@RequestParam(name = "id",required = true,defaultValue = "0") Long id,
                                     HttpServletRequest request) {
        ResultView result=null;
        try{
            if(cookieUser.isSupper(request)) {
                Category category = categoryService.getCategoryById(id);
                if(category.getCustomerId().equals(cookieUser.getCustomerId(request))) {//删除的时候验证是否是删除同一商户下面的栏目，增强安全性
                    category.setDeleted(true);
                    category.setRoute(null);
                    categoryService.save(category);
                    result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
                }
                else{
                    result=new ResultView(ResultOptionEnum.NO_LIMITS.getCode(),ResultOptionEnum.NO_LIMITS.getValue(),null);
                }
            }
            else {
                result=new ResultView(ResultOptionEnum.NO_LIMITS.getCode(),ResultOptionEnum.NO_LIMITS.getValue(),null);
            }
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage());
            result=new ResultView(ResultOptionEnum.FAILE.getCode(),ResultOptionEnum.FAILE.getValue(),null);
        }
        return  result;
    }

//    /**
//     * 新增栏目信息 xhl
//     * */
//    @RequestMapping(value = "/modifyCategory",method = RequestMethod.POST)
//    @ResponseBody
//    public ResultView modifyCategory(@RequestParam(name = "id",required = true,defaultValue = "0") Long id,
//                                     @RequestParam(name = "siteId",required = true) Long siteId,
//                                     @RequestParam(name = "name",required = true) String name,
//                                     @RequestParam(name = "modelType") Integer modelType,
//                                     @RequestParam(name = "orderWeight") Long orderWeight,
//                                     @RequestParam(name = "rule") String rule,
//                                     @RequestParam(name = "template") String template,
//                                     @RequestParam(name = "noRule") String noRule){
//        ResultView result=null;
//        try {
//            if (!routeService.isPatterBySiteAndRuleIgnore(category.getSite(), rule, noRule)) {
//                Site site = siteRepository.findOne(siteId);
//                Category categoryParent = categoryService.getCategoryById(parentId);
//                category.setSite(site);
//                if (model >= 0) {
//                    category.setModelType(model);
//                }
//                category.setCustomerId(site.getCustomerId());
//                category.setParent(categoryParent);
//                category.setUpdateTime(LocalDateTime.now());
//                categoryService.save(category);
//                result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
//            }
//        }
//        catch (Exception ex)
//        {
//            log.error(ex.getMessage());
//            result=new ResultView(ResultOptionEnum.FAILE.getCode(),ResultOptionEnum.FAILE.getValue(),null);
//        }
//        return  result;
//    }
}
