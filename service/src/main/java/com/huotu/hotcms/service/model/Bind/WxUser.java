package com.huotu.hotcms.service.model.Bind;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by chendeyu on 2016/3/31.
 */
@Setter
@Getter
public class WxUser {
    private String openid;
    private int sex;
    private String nickname;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private String unionid;
}
