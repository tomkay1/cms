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

/**
 * 临时解决方案控制器
 * Created by xhl on 2016/1/31.
 */
@Controller
@RequestMapping(value = "/interim")
public class InterimController {
    private static final Log log = LogFactory.getLog(InterimController.class);

    /**
     * 火图官网招商加盟表单交互临时解决方案 1.0 beta
     *
     * @param company 公司名称
     * @param name  姓名
     * @param mobile 手机号码
     * @param qq  QQ
     * @param province 省份
     * @param city 城市
     * @param description 描述信息
     * @param request
     * @return
     */
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
            log.fatal("公司名称:" + company + ";姓名：" + name + ";手机号码：" + mobile + ";qq:" + qq + ";省份:" + province + ";城市:" + city + "需求说明:" + description);
            resultView = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
        }catch (Exception ex){
            log.error(ex.getMessage());
            resultView = new ResultView(ResultOptionEnum.SERVERFAILE.getCode(), ResultOptionEnum.SERVERFAILE.getValue(), null);
        }
        return resultView;
    }

}
