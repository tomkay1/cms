/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.exception.LoginException;
import com.huotu.hotcms.service.exception.RegisterException;
import com.huotu.huobanplus.common.entity.Brand;
import com.huotu.huobanplus.common.entity.Category;
import com.huotu.huobanplus.common.entity.User;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * 提供伙伴商城的相关数据
 * 所有API都可能会抛出{@link IllegalArgumentException}如果商户并未开启伙伴商城
 *
 * @author CJ
 */
public interface MallService {

    /**
     * @param merchantId 商户号 {@link com.huotu.hotcms.service.entity.login.Owner#customerId}
     * @return 相关类目
     * @throws IOException {@link com.huotu.huobanplus.sdk.common.repository.CategoryRestRepository#findAll(Pageable)}
     */
    List<Category> listCategories(long merchantId) throws IOException;

    /**
     * @param merchantId 商户号 {@link com.huotu.hotcms.service.entity.login.Owner#customerId}
     * @return 品牌
     * @throws IOException
     */
    List<Brand> listBrands(long merchantId) throws IOException;

    /**
     * 获取登录的用户
     *
     * @param request 当前请求
     * @param owner   所属商户
     * @return 登录返回商城用户，未登录返回null
     * @throws IOException 通讯异常导致无法获取 {@link com.huotu.huobanplus.sdk.common.repository.UserRestRepository#getOneByPK(Serializable)}
     */
    User getLoginUser(HttpServletRequest request, Owner owner) throws IOException;

    /**
     * 用户是否登录
     *
     * @param request 当前请求
     * @param owner   所属商户
     * @return true for current user already logged.
     */
    boolean isLogin(HttpServletRequest request, Owner owner);

    /**
     * 获取当前登录用户名称
     *
     * @param request 当前请求
     * @param owner   所属商户
     * @return 用户名称，null
     * @throws IOException {@link #getLoginUser(HttpServletRequest, Owner)}
     */
    String getLoginUserName(HttpServletRequest request, Owner owner) throws IOException;


    /**
     * 获取当前商城域名
     *
     * @param owner 所属商户
     * @return 商城内购页域名
     * @throws IOException 通讯异常
     */
    String getMallDomain(Owner owner) throws IOException;

    /**
     * 商城用户登录
     * 登录成功后需要按照Cookie规则写入主domain
     *
     * @param owner    工作商户
     * @param username 登录名（手机号）
     * @param password 明文密码
     * @param response 可选响应,如果传入有效值服务应当帮助写入cookie以保证SSO
     * @return 总是会返回一个非null的结果
     * @throws IOException    通讯故障,应该建议用户重试
     * @throws LoginException 登录逻辑失败 包括但不限于系统请求失败,未找到数据,登录失败,账户被冻结,操作失败,数据库操作失败
     */
    User mallLogin(Owner owner, String username, String password, HttpServletResponse response) throws IOException, LoginException;

    /**
     * 商城用户注册
     * 登录成功后需要按照Cookie规则写入主domain
     *
     * @param owner    工作商户
     * @param username 登录名（手机号）
     * @param password 明文密码
     * @param response 可选响应,如果传入有效值服务应当帮助写入cookie以保证SSO
     * @return 总是会返回一个非null的结果
     * @throws IOException       通讯故障,应该建议用户重试
     * @throws RegisterException 注册逻辑失败 包括但不限于系统请求失败,注册失败,该用户已被注册,操作失败,数据库操作失败
     */
    User mallRegister(Owner owner, String username, String password, HttpServletResponse response) throws IOException, RegisterException;

}
