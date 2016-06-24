/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.repositoryi.LoginRepository;

import java.util.List;

/**
 * Owner仓库
 * @author CJ
 */
public interface OwnerRepository extends LoginRepository<Owner> {

    /**
     * 查找伙伴商城相关的owner
     *
     * @param customerId 伙伴商城商户号
     * @return owner
     */
    Owner findByCustomerId(int customerId);

    /**
     * 查询跟伙伴商城关联的owner
     *
     * @return owner List
     */
    List<Owner> findByCustomerIdNotNull();

}
