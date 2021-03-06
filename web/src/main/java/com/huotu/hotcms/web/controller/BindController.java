/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.web.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.huotu.hotcms.service.service.ConfigService;
import com.huotu.hotcms.service.thymeleaf.service.SiteResolveService;
import com.huotu.hotcms.web.service.GoodsDetailService;
import com.huotu.hotcms.web.service.RegisterByWeixinService;
import com.huotu.huobanplus.common.entity.Merchant;
import com.huotu.huobanplus.sdk.common.repository.MerchantRestRepository;
import com.huotu.huobanplus.sdk.common.repository.WebSiteAppConfigRestRepository;
import com.huotu.huobanplus.sdk.mall.service.MallInfoService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Locale;

/**
 * <p>
 * PC官网、商城第三方授权绑定操作
 * </p>
 *
 * @since 1.2
 */
@Controller
@RequestMapping(value = "/bind")
public class BindController {
    private static final Log log = LogFactory.getLog(BindController.class);

    @Autowired
    private RegisterByWeixinService registerByWeixinService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private MerchantRestRepository merchantRestRepository;

    @Autowired
    private SiteResolveService siteResolveService;

    @Autowired
    private WebSiteAppConfigRestRepository webSiteAppConfigRestRepository;


    @Autowired
    private MallInfoService mallInfoService;

    @Autowired
    private GoodsDetailService goodsDetailService;

    /**
     * 微信二维码注册回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/callback")
    public ModelAndView callback(HttpServletRequest request,HttpServletResponse response, RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = null;
//            try {
//                String code = request.getParameter("code");
//                String state = request.getParameter("state");//state为goodsId
//                String appid = configInfo.getAppid();
//                String secret = configInfo.getAppsecret();
//                int customerId = siteResolveService.getCurrentSite(request).getOwnerId();
//                String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + secret + "&code=" + code + "&grant_type=authorization_code" + "&state=" + state;
//                WxUser wxUser = registerByWeixinService.getWxUser(url);//返回用户信息
//                RegisterWeixinUserData registerWeixinUserData =registerByWeixinService.RegisterByWeixin(customerId,wxUser);
//                cookieUser.setUserID(response, registerWeixinUserData.getUserId().toString());
//                modelAndView = new ModelAndView("redirect:/shop/product/"+state);//redirect模式.重定向到商品详情页
//            } catch (Exception ex) {
//                log.error(ex);
//            }
        return modelAndView;
    }


    /**
     *
     * 二维码购买
     */
    @RequestMapping(value = "/qrCode", method = { RequestMethod.GET,RequestMethod.POST })
    public void qrCode(HttpServletRequest request, HttpServletResponse resp, Locale locale) throws Exception {
        int customerId = siteResolveService.getCurrentSite(request, locale).getOwner().getCustomerId();
        Merchant merchant;
        String num = request.getParameter("num");//购买数量
        String productId = request.getParameter("productId");//购买的产品Id
        String goodsId = request.getParameter("goodsId");//商品id
        String subDomain = "";
        try {
            merchant = merchantRestRepository.getOneByPK(customerId);
            subDomain = merchant.getSubDomain();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("接口服务不可用");
        }
        String url = configService.getCustomerUri(subDomain)+"/Mall/View.aspx?customerid="+customerId
                +"&productId="+productId+"&goodsId="+goodsId+"&num="+num;
        if (url != null && !"".equals(url)) {
            ServletOutputStream stream = null;
            try {
                int width = 400;//图片的宽度
                int height = 400;//高度
                stream = resp.getOutputStream();
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix m = writer.encode(url, BarcodeFormat.QR_CODE, height, width);
                MatrixToImageWriter.writeToStream(m, "png", stream);
            } catch (WriterException e) {
                log.error(e);
            } finally {
                if (stream != null) {
                    stream.flush();
                    stream.close();
                }
            }
        }
    }


    /**
     *
     * 二维码公众号
     */
    @RequestMapping(value = "/subscribeCode", method = { RequestMethod.GET })
    public void subscribeCode(HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
        // 指定生成的响应是图片
        int customerId = siteResolveService.getCurrentSite(request, locale).getOwner().getCustomerId();
        BufferedImage bufferedImage = webSiteAppConfigRestRepository.imageForMerchantWeixinBusinessCard(customerId);
        ImageIO.write(bufferedImage,"JPEG",response.getOutputStream());
    }

    /**
     *
     * 个人中心二维码
     */
    @RequestMapping(value = "/personCode", method = { RequestMethod.GET })
    public void personCode(HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
        String url = goodsDetailService.getPersonDetailUrl(request, locale);//获取二维码域名（商城个人中心）
        if (url != null && !"".equals(url)) {
            ServletOutputStream stream = null;
            try {
                int width = 245;//图片的宽度
                int height = 245;//高度
                stream = response.getOutputStream();
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix m = writer.encode(url, BarcodeFormat.QR_CODE, height, width);
                MatrixToImageWriter.writeToStream(m, "png", stream);
            } catch (WriterException e) {
                log.error(e);
            } finally {
                if (stream != null) {
                    stream.flush();
                    stream.close();
                }
            }
        }
    }



}





