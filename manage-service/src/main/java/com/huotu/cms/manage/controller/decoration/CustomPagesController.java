package com.huotu.cms.manage.controller.decoration;

import com.huotu.hotcms.service.entity.CustomPages;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.CustomPagesRepository;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.service.widget.service.PageResolveService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 *     商户自定义页面
 * </p>
 * @since xhl
 *
 * @version 1.2
 */
@Controller
@RequestMapping("/customPages")
public class CustomPagesController {
    private static final Log log = LogFactory.getLog(CustomPagesController.class);

    @Autowired
    private CustomPagesRepository customPagesRepository;

    @Autowired
    private PageResolveService pageResolveService;

    @Autowired
    private SiteService siteService;

    @RequestMapping("/{id}")
    public ModelAndView customPages(
                                       @RequestParam("siteId") Long siteId,
                                       @PathVariable("id") Long id){
        ModelAndView modelAndView = new ModelAndView();
        try {
            Integer isExists=0;
            try{
                CustomPages customPages=customPagesRepository.findOne(id);
                isExists=customPages!=null?1:0;
            }catch (Exception ex){
                ex.getStackTrace();
            }
            Site site=siteService.getSite(siteId);
            modelAndView.setViewName(String.format("%s_%s.shtml", siteId, id));
            modelAndView.addObject("exists",isExists);
        }catch (Exception ex){
            log.error(ex);
        }
        return modelAndView;
    }
}
