package com.huotu.hotcms.admin.controller;

import com.huotu.hotcms.admin.util.web.CookieUser;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chendeyu on 2015/12/29.
 */
@Controller
@RequestMapping("/region")
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
    public ModelAndView regionList(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/system/regionList.html");
        return  modelAndView;
    }

    /*
    * 增加地区
    * */
    @RequestMapping("/addRegion")
    public ModelAndView addRegion(@RequestParam(value = "id", defaultValue = "0") Long id) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/system/addRegion.html");
        return modelAndView;
    }

    /*
    * 修改地区
    * */
    @RequestMapping("/updateRegion")
    public ModelAndView updateRegion(@RequestParam(value = "id",defaultValue = "0") Long id) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        if(id!=0) {
            Region region = regionRepository.findOne(id);
            if (region != null) {
                modelAndView.addObject("region", region);
            }
        }
        modelAndView.setViewName("/view/system/updateRegion.html");
        return modelAndView;
    }

    /*
   * 更新地区
   * */
    @RequestMapping(value = "/saveRegion",method = RequestMethod.POST)
    @ResponseBody
    public ResultView updateRegion(@RequestParam(name ="id",required = false,defaultValue = "0") Long id,
                                  @RequestParam(name = "regionCode",required = true) String regionCode,
                                  @RequestParam(name = "regionName",required = true) String regionName,
                                  @RequestParam(name="langCode",required = false) String langCode,
                                  @RequestParam(name="langName",required = false) String langName,
                                  @RequestParam(name="langTag",required = false) String langTag){
        ResultView result=null;
        try {
            Region region = new Region();
            if(id!=0)
            {
                region= regionRepository.findOne(id);
                if (region != null) {
                    region.setLangCode(langCode);
                    region.setLangName(langName);
                    region.setLangTag(langTag);
                    region.setRegionCode(regionCode);
                    region.setRegionName(regionName);
                    regionRepository.save(region);
                }
            }
            else {
                region.setLangCode(langCode);
                region.setLangName(langName);
                region.setLangTag(langTag);
                region.setRegionCode(regionCode);
                region.setRegionName(regionName);
                regionRepository.save(region);
            }
            result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage());
            result=new ResultView(ResultOptionEnum.FAILE.getCode(),ResultOptionEnum.FAILE.getValue(),null);
        }
        return  result;
    }

    /*
    * 获得模型列表
    * */
    @RequestMapping(value = "/getRegionList",method = RequestMethod.POST)
    @ResponseBody
    public PageData<Region> getModelList(@RequestParam(name="name",required = false) String name,
                                            @RequestParam(name = "page",required = true,defaultValue = "1") Integer page,
                                            @RequestParam(name = "pagesize",required = true,defaultValue = "20") Integer pageSize){
        PageData<Region> pageModel=regionService.getPage(name, page, pageSize);
        return pageModel;
    }

    /*
    * 删除地区
    * */
    @RequestMapping(value = "/deleteRegion",method = RequestMethod.POST)
    @ResponseBody
    public ResultView deleteRegion(@RequestParam(name = "id",required = true,defaultValue = "0") Long id,HttpServletRequest request) {
        ResultView result=null;
        try{
            if(cookieUser.isSupper(request)) {
                regionRepository.delete(id);
                result=new ResultView(ResultOptionEnum.OK.getCode(),ResultOptionEnum.OK.getValue(),null);
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
}

