/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.util.web.CookieUser;
import com.huotu.hotcms.service.entity.Region;
import com.huotu.hotcms.service.repository.RegionRepository;
import com.huotu.hotcms.service.service.RegionService;
import com.huotu.hotcms.service.util.PageData;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Created by chendeyu on 2015/12/29.
 */
@Controller
@RequestMapping("/manage/region")
public class RegionController {
    private static final Log log = LogFactory.getLog(RegionController.class);

    @Autowired
    private CookieUser cookieUser;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private RegionService regionService;

    /*
    * 地区列表
    * */
    @RequestMapping("/regionList")
    public ModelAndView regionList() throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/view/system/regionList.html");
        return modelAndView;
    }

    /*
    * 增加地区
    * */
    @RequestMapping("/addRegion")
    public ModelAndView addRegion() throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/view/system/addRegion.html");
        return modelAndView;
    }

    /*
    * 修改地区
    * */
    @RequestMapping("/updateRegion")
    public ModelAndView updateRegion(@RequestParam Locale id) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        Region region = regionRepository.findOne(id);
        if (region != null) {
            modelAndView.addObject("region", region);
        }
        modelAndView.setViewName("/view/system/updateRegion.html");
        return modelAndView;
    }

    /*
   * 更新地区
   * */
    @RequestMapping(value = "/saveRegion", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public ResultView updateRegion(@RequestParam Locale id,
//                                  @RequestParam(name = "regionCode") String regionCode,
                                   @RequestParam(name = "regionName") String regionName,
//                                  @RequestParam(name="langCode",required = false) String langCode,
                                   @RequestParam(name = "langName", required = false) String langName
//                                  @RequestParam(name="langTag",required = false) String langTag
    ) {
        ResultView result;
        try {
            Region region = regionRepository.findOne(id);
            if (region == null) {
                region = new Region();
                region.setLocale(id);
            }

            region.setLangName(langName);
            region.setRegionName(regionName);
            regionRepository.save(region);

            result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            result = new ResultView(ResultOptionEnum.FAILE.getCode(), ResultOptionEnum.FAILE.getValue(), null);
        }
        return result;
    }

    /*
    * 获得模型列表
    * */
    @RequestMapping(value = "/getRegionList", method = RequestMethod.POST)
    @ResponseBody
    public PageData<Region> getModelList(@RequestParam(name = "name", required = false) String name,
                                         @RequestParam(name = "page", defaultValue = "1") Integer page,
                                         @RequestParam(name = "pagesize", defaultValue = "20") Integer pageSize) {
        return regionService.getPage(name, page, pageSize);
    }

    /*
    * 删除地区
    * */
    @RequestMapping(value = "/deleteRegion", method = RequestMethod.POST)
    @ResponseBody
    public ResultView deleteRegion(@RequestParam Locale id, HttpServletRequest request) {
        ResultView result = null;
        try {
            if (cookieUser.isSupper(request)) {
                regionRepository.delete(id);
                result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
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

