package com.huotu.hotcms.service.model.Bind;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by chendeyu on 2016/3/31.
 */
@Getter
@Setter
public class AccessToken {
    private String access_token;
    private String expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
}
