//package com.huotu.hotcms.service.widget.service;
//
//import com.alibaba.fastjson.JSON;
//import com.huotu.hotcms.service.config.ServiceTestConfig;
//import com.huotu.hotcms.service.model.Bind.WxUser;
//import com.huotu.hotcms.service.widget.model.GoodsDetail;
//import com.huotu.huobanplus.common.entity.Merchant;
//import com.huotu.huobanplus.sdk.common.repository.MerchantRestRepository;
//import com.huotu.huobanplus.sdk.mall.model.RegisterWeixinUserData;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by chendeyu on 2016/4/8.
// */
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = ServiceTestConfig.class)
//@WebAppConfiguration
//@Transactional
//
//public class GoodsDetailServiceTest {
//
//    @Autowired
//    private GoodsDetailService goodsDetailService;
//    @Autowired
//    private RegisterByWeixinService registerByWeixinService;
//
//    @Autowired
//    private MallApiEnvironmentService mallApiEnvironmentService;
//
//    @Autowired
//    private MerchantRestRepository merchantRestRepository;
//
//    @Test
//    public void getGoodsDetailTest() throws Exception{
//        GoodsDetail goods = goodsDetailService.getGoodsDetail(1,28);
//        System.out.println(goods);
//    }
//
//    @Test
//    public void re() throws Exception{
//        WxUser wxUser = new WxUser();
//        wxUser.setOpenid(0+"1212");
//        wxUser.setNickname("biu");
//        wxUser.setCity("hangz");
//        wxUser.setCountry("china");
//        wxUser.setHeadimgurl("baidu.com");
//        wxUser.setProvince("binjiang");
//        wxUser.setSex(1);
//        wxUser.setUnionid("o2NXPw_LEa5Dun95lwZpyVPGCyuE");
//        wxUser.setUserId(1);
//        RegisterWeixinUserData registerWeixinUserData =registerByWeixinService.RegisterByWeixin(4471, wxUser);
//        System.out.print(registerWeixinUserData);
////        Merchant merchant = merchantRestRepository.getOneByPK(4471);
////        System.out.print(merchant);
//
//    }
//
//    @Test
//    public void testMerchant() {
//        Merchant merchant = null;
//        try {
//            merchant = merchantRestRepository.getOneByPK(4471);
//            String subDomain = merchant.getSubDomain();
//            String url = mallApiEnvironmentService.getCustomerUri(subDomain)+".aspx?";
//            System.out.print(url);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.print(merchant);
//    }
//
//    @Test
//    public void test() {
//        String spec  ="{\"1\":\"颜色\",\"2\":\"尺码\"}";
//        Map map = new HashMap<>();
//        map.put("1","颜色");
//        map.put("2","尺码");
//        Map map1 = JSON.parseObject(spec, Map.class);
//        System.out.print(map);
//    }
//
//
//    @Test
//    public void test2() {
//        String info  = "<ul class=\"attributes-list list-paddingleft-2\" style=\"list-style-type: none;\"><li><p>产品名称：AVENT/新安怡 SCF660/17</p></li><li><p>品牌: AVENT/新安怡</p></li><li><p>货号: SCF660/17</p></li><li><p>容量: 125ML</p></li><li><p>奶瓶是否带柄: 不带柄</p></li><li><p>材质: 其他</p></li><li><p>商品条形码: 198800</p></li><li><p>口径大小: 宽口径</p></li><li><p>形状: 弧形</p></li></ul><p><img src=\"/resource/content/html/images/20140419130611.jpg\" title=\"1.jpg\"/></p><p><img src=\"/resource/content/html/images/20140419130642.jpg\" style=\"float:none;\" title=\"2.jpg\"/></p><p><img src=\"/resource/content/html/images/20140419130729.jpg\" title=\"1.jpg\"/></p><p><img src=\"/resource/content/html/images/20140419130843.jpg\" style=\"float:none;\" title=\"2.jpg\"/></p><p><img src=\"/resource/content/html/images/20140419130844.jpg\" style=\"float:none;\" title=\"3.jpg\"/></p><p><img src=\"/resource/content/html/images/20140419130845.jpg\" style=\"float:none;\" title=\"4.jpg\"/></p><p><img src=\"/resource/content/html/images/20140419130846.jpg\" style=\"float:none;\" title=\"5.jpg\"/></p><p><img src=\"/resource/content/html/images/20140419130847.jpg\" style=\"float:none;\" title=\"6.jpg\"/></p><p><img src=\"/resource/content/html/images/20140419130848.jpg\" style=\"float:none;\" title=\"7.jpg\"/></p><p><img src=\"/resource/content/html/images/20140419130849.jpg\" style=\"float:none;\" title=\"8.jpg\"/></p><p><img src=\"/resource/content/html/images/20140419130851.jpg\" style=\"float:none;\" title=\"9.jpg\"/></p><p><img src=\"/resource/content/html/images/20140419130852.jpg\" style=\"float:none;\" title=\"10.jpg\"/></p><p><img src=\"/resource/content/html/images/20140419130853.jpg\" style=\"float:none;\" title=\"11.jpg\"/></p><p><br/></p>";
//        String info1 =info.replace("img src=\"/", "img src=\""+mallApiEnvironmentService.getImgUri("")+"/");
//        System.out.print(info);
//    }
//
//
//}
