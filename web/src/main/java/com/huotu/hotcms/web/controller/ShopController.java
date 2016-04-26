package com.huotu.hotcms.web.controller;

import com.huotu.hotcms.service.common.PageErrorType;
import com.huotu.hotcms.service.entity.CustomPages;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.service.CustomPagesService;
import com.huotu.hotcms.service.thymeleaf.service.SiteResolveService;
import com.huotu.hotcms.service.thymeleaf.templateresource.WidgetTemplateResource;
import com.huotu.hotcms.service.widget.model.GoodsDetail;
import com.huotu.hotcms.service.widget.service.PageResourceService;
import com.huotu.hotcms.web.service.GoodsDetailService;
import com.huotu.hotcms.web.util.web.CookieUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 商城入口Controller
 * </p>
 *
 * @since xhl
 */
@Controller
@RequestMapping(value = "/shop")
public class ShopController {
    private static final Log log = LogFactory.getLog(ShopController.class);

    @Autowired
    private CustomPagesService customPagesService;

    @Autowired
    private GoodsDetailService goodsDetailService;

    @Autowired
    SiteResolveService siteResolveService;

    @Autowired
    private CookieUser cookieUser;

    @Autowired
    private PageResourceService pageResourceService;

    private WidgetTemplateResource widgetTemplateResource=new WidgetTemplateResource();

    @Autowired
    private Environment environment;

    /**
     * 商城首页/shop/
     * */
    @RequestMapping("/")
    public ModelAndView index(HttpServletRequest request){
        ModelAndView modelAndView=new ModelAndView();
        try{
            Site site = siteResolveService.getCurrentSite(request);
            CustomPages customPages=customPagesService.findHomePages(site);
            if(customPages!=null){
                modelAndView.setViewName(String.format("%s_%s.cshtml", site.getSiteId(),customPages.getId()));
            }else{
                modelAndView.setViewName(PageErrorType.BUDDING_500.getValue());
            }
        }catch (Exception ex){
            log.error(ex);
        }
        return modelAndView;
    }

    /**
     * 商城其他页面-->/shop/{id}/........
     * **/
    @RequestMapping("/{name}")
    public ModelAndView page(HttpServletRequest request, @PathVariable("name") String id){
        ModelAndView modelAndView = new ModelAndView();
        try {
            Site site = siteResolveService.getCurrentSite(request);
            modelAndView.setViewName(String.format("%s_%s.cshtml", site.getSiteId(),id));
        } catch (Exception ex) {
            log.error(ex);
        }
        return modelAndView;
    }

    /**
     * 商品详情页面
     * **/
    @RequestMapping("/product/{id}")
    public ModelAndView defaultsPage(HttpServletRequest request, @PathVariable("id") String id){
        ModelAndView modelAndView = new ModelAndView();
        try{
            modelAndView.addObject("unlogin", cookieUser.checkLogin(request));//未登录为false，登录了则为true
            int userId = 0;
            if(cookieUser.checkLogin(request)){
                 userId = Integer.valueOf(cookieUser.getUserId(request));
            }
            Site site = siteResolveService.getCurrentSite(request);
            String head = pageResourceService.getHeaderTemplateBySite(site);//公共头部
            if(head == null) {
                head = "";
            }else{
                head=widgetTemplateResource.getHtmlHeadStaticResources(environment)+head;
            }
            GoodsDetail goods = goodsDetailService.getGoodsDetail(Integer.valueOf(id),userId);
            String personDetailUrl = goodsDetailService.getPersonDetailUrl(request);//获取二维码域名（商城个人中心）
            String subscribeUrl = goodsDetailService.getSubscribeUrl(request);//获取二维码域名（商户公众号）
            modelAndView.setViewName("/template/0/goodsDetail.html");
            modelAndView.addObject("goods",goods);
            modelAndView.addObject("site",site);//为了传递seo
            modelAndView.addObject("products",goods.getProducts());
            modelAndView.addObject("head",head);//公共头部
            modelAndView.addObject("personDetailUrl",personDetailUrl);//获取二维码域名（商城个人中心）
            modelAndView.addObject("subscribeUrl",subscribeUrl);//获取二维码域名（商户公众号）

        }catch (Exception ex){
            log.error(ex);
        }
        return modelAndView;
    }
}
