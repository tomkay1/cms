package com.huotu.hotcms.admin.controller.decoration;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by hzbc on 2016/5/26.
 */
@Controller
@RequestMapping("/template")
public class TemplatesController {
    @Autowired
    private SiteService siteService;
    @Autowired
    private SiteRepository siteRepository;

    @RequestMapping("/use")
    @ResponseBody
    public ResultView useTemplate(long templateId,long siteId) {
        Site customerSite=siteRepository.findOne(siteId);
        try {
            siteService.siteCopy(templateId,customerSite);
            return new ResultView(ResultOptionEnum.OK.getCode(),ResultOptionEnum.OK.getValue(),null);
        } catch (Exception e) {
            return new ResultView(ResultOptionEnum.FAILE.getCode(),ResultOptionEnum.FAILE.getValue(),null);
        }

    }

    @RequestMapping("/view")
    public String viewTemplate(long templateId,@RequestParam(required = false) long pageId){
        return "";
    }


}
