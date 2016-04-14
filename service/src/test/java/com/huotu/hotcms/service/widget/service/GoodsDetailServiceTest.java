package com.huotu.hotcms.service.widget.service;

import com.alibaba.fastjson.JSON;
import com.huotu.hotcms.service.config.ServiceTestConfig;
import com.huotu.hotcms.service.model.Bind.WxUser;
import com.huotu.hotcms.service.widget.model.GoodsDetail;
import com.huotu.huobanplus.sdk.mall.model.RegisterWeixinUserData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chendeyu on 2016/4/8.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceTestConfig.class)
@WebAppConfiguration
@Transactional

public class GoodsDetailServiceTest {

    @Autowired
    private GoodsDetailService goodsDetailService;
    @Autowired
    private RegisterByWeixinService registerByWeixinService;

    @Test
    public void getGoodsDetailTest() throws Exception{
        GoodsDetail goods = goodsDetailService.getGoodsDetail(1,28);
        System.out.println(goods);
    }

    @Test
    public void re() throws Exception{
        WxUser wxUser = new WxUser();
        wxUser.setOpenid(0+"");
        wxUser.setNickname("biu");
        wxUser.setCity("hangz");
        wxUser.setCountry("china");
        wxUser.setHeadimgurl("baidu.com");
        wxUser.setProvince("binjiang");
        wxUser.setSex(1);
        wxUser.setUnionid("o2NXPw_LEa5Dun95lwZpyVPGCyuE");
        wxUser.setUserId(1);
        RegisterWeixinUserData registerWeixinUserData =registerByWeixinService.RegisterByWeixin(4471, wxUser);
        System.out.print(registerWeixinUserData);
    }

    @Test
    public void test() {
        String spec  ="{\"1\":\"颜色\",\"2\":\"尺码\"}";
        Map map = new HashMap<>();
        map.put("1","颜色");
        map.put("2","尺码");
        Map map1 = JSON.parseObject(spec, Map.class);
        System.out.print(map);
    }


}
