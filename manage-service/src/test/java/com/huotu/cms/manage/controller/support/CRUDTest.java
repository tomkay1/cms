/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller.support;

import com.huotu.cms.manage.page.support.AbstractCRUDPage;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * 测试流程是
 * <ul>
 * <li>构造数据</li>
 * <li>打开网页</li>
 * <li>填写表单</li>
 * <li>提交表单</li>
 * <li>查看列表</li>
 * <li>校对列表</li>
 * <li>新增资源校验</li>
 * <li>打开某项资源详情（可选）</li>
 * <li>填写表单（可选）</li>
 * <li>提交表单（可选）</li>
 * <li>新增资源校验</li>
 * </ul>
 *
 * @author CJ
 */
public interface CRUDTest<T> {

    /**
     * @return 是否允许修改资源
     */
    default boolean modify() {
        return false;
    }

    /**
     * @return 业务层的相关业务数据
     */
    Collection<T> list() throws Exception;

    /**
     * @return 随机的业务数据
     */
    T randomValue() throws Exception;

    /**
     * @return 除了page还有别的表单操作么?
     */
    BiConsumer<AbstractCRUDPage<T>, T> customAddFunction() throws Exception;

    /**
     * 检查新增的数据是否正确
     *
     * @param entity 来自JPA
     * @param data   数据
     */
    void assertCreation(T entity, T data) throws Exception;

    /**
     * @return 可编辑属性的过滤器
     */
    default Predicate<? super PropertyDescriptor> editableProperty() throws Exception {
        return (x) -> true;
    }
}