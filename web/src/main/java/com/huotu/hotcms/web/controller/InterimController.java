package com.huotu.hotcms.web.controller;

import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.RandomAccessFile;

/**
 * 临时解决方案控制器
 * Created by xhl on 2016/1/31.
 */
@Controller
@RequestMapping(value = "/interim")
public class InterimController {
    private static final Log log = LogFactory.getLog(InterimController.class);

    @RequestMapping(value = "/join",method = RequestMethod.POST)
    @ResponseBody
    public ResultView join(@RequestParam(value = "company") String company,
                           @RequestParam(value="name") String name,
                           @RequestParam(value = "mobile") String mobile,
                           @RequestParam(value = "qq") String qq,
                           @RequestParam(value = "province") String province,
                           @RequestParam(value = "city") String city,
                           @RequestParam(value = "desciption") String description,HttpServletRequest request){
        ResultView resultView=null;
        try{
            log.info("公司名称:"+company+"   ;姓名："+name+"   ; 手机号码："+mobile+" ; qq:"+qq+"  ;省份:"+province+"  ;城市:"+city+" 需求说明:"+description);
            resultView = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
        }catch (Exception ex){
            log.error(ex.getMessage());
            resultView = new ResultView(ResultOptionEnum.SERVERFAILE.getCode(), ResultOptionEnum.SERVERFAILE.getValue(), null);
        }
        return resultView;
    }
}
