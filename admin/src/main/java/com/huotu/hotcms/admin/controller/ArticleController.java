package com.huotu.hotcms.admin.controller;

import com.huotu.hotcms.admin.util.web.CookieUser;
import com.huotu.hotcms.service.common.ArticleSource;
import com.huotu.hotcms.service.common.EnumUtils;
import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.model.ArticleCategory;
import com.huotu.hotcms.service.repository.ArticleRepository;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.service.ArticleService;
import com.huotu.hotcms.service.util.PageData;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import com.huotu.hotcms.service.widget.service.StaticResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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
    private static final Log log = LogFactory.getLog(ArticleController.class);

    @Autowired
    private ArticleService articleService;
    @Autowired
    private StaticResourceService resourceServer;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CookieUser cookieUser;

    @Autowired
    private ArticleRepository articleRepository;

    /**
     * 文章分页
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/articleList")
    public ModelAndView articleList(@RequestParam(value = "id", defaultValue = "0") Long id) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.setViewName("/view/contents/articleList.html");
            Article article = articleService.findById(id);
            String logo_uri = "";
            if (!StringUtils.isEmpty(article.getThumbUri())) {
                logo_uri = resourceServer.getResource(article.getThumbUri()).toString();
            }
            modelAndView.addObject("logo_uri", logo_uri);
            modelAndView.addObject("article", article);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return modelAndView;
    }


    /**
     * 添加文章
     *
     * @param customerId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addArticle")
    public ModelAndView addArticle(Integer customerId) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/view/widget/addArticle.html");
        return modelAndView;
    }

    /**
     * 修改文章
     *
     * @param id
     * @param customerId
     * @return
     * @throws Exception
     */
    @RequestMapping("/updateArticle")
    public ModelAndView updateArticle(@RequestParam(value = "id", defaultValue = "0") Long id, Integer customerId) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.setViewName("/view/contents/updateArticle.html");
            Article article = articleService.findById(id);
            String logo_uri = "";
            if (!StringUtils.isEmpty(article.getThumbUri())) {
                logo_uri = resourceServer.getResource(article.getThumbUri()).toString();
            }
            Category category = article.getCategory();
            Integer modelType = category.getModelId();
            Set<Category> categorys = categoryRepository.findByCustomerIdAndModelId(customerId, modelType);
            modelAndView.addObject("logo_uri", logo_uri);
            modelAndView.addObject("categorys", categorys);
            modelAndView.addObject("article", article);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return modelAndView;
    }


    /**
     * 保存文章
     *
     * @param article
     * @param isSystem
     * @param categoryId
     * @param articleSourceId
     * @return
     */
    @RequestMapping(value = "/saveArticle", method = RequestMethod.POST)
    @Transactional(value = "transactionManager")
    @ResponseBody
    public ResultView saveArticle(Article article, Boolean isSystem, Long categoryId, int articleSourceId) {
        ResultView result = null;
        try {
            Long id = article.getId();
            Category category = categoryRepository.getOne(categoryId);
            ArticleSource articleSource = EnumUtils.valueOf(ArticleSource.class, articleSourceId);
            if (id != null) {
                Article articleOld = articleService.findById(article.getId());
                article.setCreateTime(articleOld.getCreateTime());
                article.setUpdateTime(LocalDateTime.now());
                article.setLauds(articleOld.getLauds());
                article.setScans(articleOld.getScans());
                article.setUnlauds(articleOld.getUnlauds());
            } else {
                article.setCreateTime(LocalDateTime.now());
                article.setUpdateTime(LocalDateTime.now());
            }
            article.setArticleSource(articleSource);
            article.setSystem(isSystem);
            article.setCategory(category);
            articleService.saveArticle(article);
            result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            result = new ResultView(ResultOptionEnum.FAILE.getCode(), ResultOptionEnum.FAILE.getValue(), null);
        }
        return result;
    }


    /**
     * 文章分页
     *
     * @param customerId
     * @param title
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/getArticleList")
    @ResponseBody
    public PageData<ArticleCategory> getArticleList(@RequestParam(name = "customerId", required = false) Integer customerId,
                                                    @RequestParam(name = "title", required = false) String title,
                                                    @RequestParam(name = "page", required = true, defaultValue = "1") int page,
                                                    @RequestParam(name = "pagesize", required = true, defaultValue = "20") int pageSize) {
        PageData<ArticleCategory> pageModel = null;
        try {
            pageModel = articleService.getPage(customerId, title, page, pageSize);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return pageModel;
    }

    /**
     * 删除文章,只有管理员才可做删除操作
     *
     * @param id
     * @param customerId
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteArticle", method = RequestMethod.POST)
    @ResponseBody
    public ResultView deleteArticle(@RequestParam(name = "id", required = true, defaultValue = "0") Long id, int customerId, HttpServletRequest request) {
        ResultView result = null;
        try {
            if (cookieUser.getCustomerId(request) == customerId) {
                Article article = articleService.findById(id);
                if (article.isSystem() == true) {
                    result = new ResultView(ResultOptionEnum.SYSTEM_ARTICLE.getCode(), ResultOptionEnum.SYSTEM_ARTICLE.getValue(), null);
                    return result;
                } else {
                    articleRepository.delete(article);
//                    article.setDeleted(true);
//                    articleService.saveArticle(article);
                    result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
                }
            } else {
                result = new ResultView(ResultOptionEnum.NO_LIMITS.getCode(), ResultOptionEnum.NO_LIMITS.getValue(), null);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            result = new ResultView(ResultOptionEnum.FAILE.getCode(), ResultOptionEnum.FAILE.getValue(), null);
        }
        return result;
    }


}