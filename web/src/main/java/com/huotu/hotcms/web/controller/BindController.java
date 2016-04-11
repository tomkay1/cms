package com.huotu.hotcms.web.controller;

import com.huotu.hotcms.service.common.ConfigInfo;
import com.huotu.hotcms.service.model.Bind.WxUser;
import com.huotu.hotcms.service.thymeleaf.model.RequestModel;
import com.huotu.hotcms.service.thymeleaf.service.RequestService;
import com.huotu.hotcms.service.widget.service.RegisterByWeixinService;
import com.huotu.hotcms.web.util.QRCodeUtil;
import com.huotu.hotcms.web.util.web.CookieUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

/**
 * <p>
 * PC官网、商城第三方授权绑定操作
 * </p>
 *
 * @since xhl
 */
@Controller
@RequestMapping(value = "/bind")
public class BindController {
    private static final Log log = LogFactory.getLog(ShopController.class);

    @Autowired
    private RequestService requestService;

    @Autowired
    private RegisterByWeixinService registerByWeixinService;


    @Autowired
    private CookieUser cookieUser;




    @Autowired
    private ConfigInfo configInfo;


    @RequestMapping("/callback")
    public ModelAndView callback(HttpServletRequest request,HttpServletResponse response){
        ModelAndView modelAndView=new ModelAndView();

        if (cookieUser.checkLogin(request,response)) {//已登录

        }
        else {//未登录扫码登录
            try {
                modelAndView.setViewName("/template/0/goodsDetail.html");
                RequestModel requestModel = requestService.ConvertRequestModelByError(request);
                modelAndView.addObject("localUrl", requestModel.getRoot());
                String code = request.getParameter("code");
                String state = request.getParameter("state");
                String appid = configInfo.getAppid();
                String secret = configInfo.getAppsecret();
                String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + secret + "&code=" + code + "&grant_type=authorization_code" + "&state=" + state;
                WxUser wxUser = registerByWeixinService.getWxUser(url);
                modelAndView.addObject("wxUser", wxUser);
                cookieUser.setUnionID(response, wxUser.getUnionid());
            } catch (Exception ex) {
                log.error(ex);
            }
        }
        return modelAndView;
    }


    @RequestMapping("/QrCode")
    public void QrCode(HttpServletRequest request,HttpServletResponse response){
        ModelAndView modelAndView=new ModelAndView();
        try {
            QRCodeUtil qrCodeUtil = new QRCodeUtil();
            BufferedImage bi = qrCodeUtil.createImage("http://baidu.com");
            OutputStream os=response.getOutputStream();
            ImageIO.write(bi, "jpg",os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
