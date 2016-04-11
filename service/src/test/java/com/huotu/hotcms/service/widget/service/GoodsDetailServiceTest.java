package com.huotu.hotcms.service.widget.service;

import com.huotu.hotcms.service.config.ServiceTestConfig;
import com.huotu.huobanplus.common.entity.Goods;
import com.huotu.huobanplus.sdk.mall.model.RegisterWeixinUserData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

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
        Goods goods = goodsDetailService.getGoodsDetail(100);
        System.out.println(goods);
    }

    @Test
    public void re() throws Exception{
        RegisterWeixinUserData registerWeixinUserData =registerByWeixinService.RegisterByWeixin(4471,0,"biu","ooEpQwXqMYmwRZ_P0dR_FpvybLZM","hangz","china","zhejiang","http://baidu.com","o2NXPw_LEa5Dun95lwZpyVPGCyuE");
        System.out.print(registerWeixinUserData);
    }


}
