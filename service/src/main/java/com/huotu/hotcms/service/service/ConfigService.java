package com.huotu.hotcms.service.service;

/**
 * 相对固定的系统属性服务
 */
public interface ConfigService {

    /**
     *
     * @return 商城资源地址 结尾没有/
     */
    String getMallResourceURL();

    /**
     * 这个CMS系统所对应的商城域名
     *
     * @return 域名
     */
    String getMallDomain();

    /**
     * 根据环境获取商店二维码前缀地址
     * @param domain 商户账号名
     * @return
     */
    String getCustomerUri(String domain);

    /**
     * 根据环境获取商品详情的图片前缀地址
     * @param domain 商户账号名
     * @return
     */
    String getImgUri(String domain);
}
