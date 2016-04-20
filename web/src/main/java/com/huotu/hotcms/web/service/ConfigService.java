package com.huotu.hotcms.web.service;

/**
 * Created by Administrator on 2016/4/19.
 */
public interface ConfigService {

    /**
     * 根据环境获取商店二维码前缀地址
     * @param domain
     * @return
     */
    String getCustomerUri(String domain);

    /**
     * 根据环境获取商品详情的图片前缀地址
     * @param domain
     * @return
     */
    String getImgUri(String domain);
}
