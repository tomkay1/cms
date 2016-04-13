package com.huotu.hotcms.web.controller;

import com.huotu.hotcms.service.common.ConfigInfo;
import com.huotu.hotcms.service.model.Bind.WxUser;
import com.huotu.hotcms.service.thymeleaf.service.RequestService;
import com.huotu.hotcms.service.widget.service.GoodsDetailService;
import com.huotu.hotcms.service.widget.service.RegisterByWeixinService;
import com.huotu.hotcms.web.util.QRCodeUtil;
import com.huotu.hotcms.web.util.web.CookieUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private GoodsDetailService goodsDetailService;


    @Autowired
    private ConfigInfo configInfo;

    @Autowired
    private CookieUser cookieUser;

    /**
     *
     * 微信二维码注册
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/callback")
    public ModelAndView callback(HttpServletRequest request,HttpServletResponse response, RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = null;
            try {
                String code = request.getParameter("code");
                String state = request.getParameter("state");//state为goodsId
                String appid = configInfo.getAppid();
                String secret = configInfo.getAppsecret();
                String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + secret + "&code=" + code + "&grant_type=authorization_code" + "&state=" + state;
                WxUser wxUser = registerByWeixinService.getWxUser(url);
                cookieUser.setUnionID(response, wxUser.getUnionid());
                modelAndView = new ModelAndView("redirect:/shop/product/"+state);//redirect模式.重定向到商品详情页
            } catch (Exception ex) {
                log.error(ex);
            }
        return modelAndView;
    }

    /**
     *
     * 二维码购买
     * @param request
     * @param response
     */
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
