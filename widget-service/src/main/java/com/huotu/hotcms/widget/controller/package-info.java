/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

/**
 * <p>页面管理服务</p>
 * <i>提供页面的管理服务，这些服务的使用对象是CMS系统中的页面管理网页。
 * 页面管理服务应该直接提供HTTP可用的接口，正文内容都应该按照HTTP常见约定，并且使用 application/json 内容类型。</i>
 * <p>
 * <ul>
 *
 * <li>获取页面 GET /owners/{ownerId}/pages</li>
 *
 * <li>保存页面 PUT /pages/{id}</li>
 *
 * <li>新增页面 POST  /owners/{ownerId}/pages</li>
 *
 * <li>删除页面 DELETE /pages/{id}</li>
 *
 * <li>保存页面部分属性 PUT /pages/{id}/{propertyName}</li>
 * </ul>
 *
 * @author CJ
 */
package com.huotu.hotcms.widget.controller;