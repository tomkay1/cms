package com.huotu.hotcms.web.service;

import com.huotu.hotcms.service.model.Bind.WxUser;
import com.huotu.huobanplus.sdk.mall.model.RegisterWeixinUserData;

/**
 * Created by chendeyu on 2016/4/8.
 * 微信注册，如已注册则返回个人信息
 */

public interface RegisterByWeixinService {

    RegisterWeixinUserData RegisterByWeixin(int customerId,WxUser wxUser);


    WxUser getWxUser(String url);
}
