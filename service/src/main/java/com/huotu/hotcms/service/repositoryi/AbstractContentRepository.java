/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.repositoryi;

import com.huotu.hotcms.service.entity.AbstractContent;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 真的抽象内容仓库
 *
 * @author CJ
 */
//@NoRepositoryBean
public interface AbstractContentRepository<T extends AbstractContent>
        extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

    /**
     * @param category 数据源
     * @return 按数据源查找
     */
    List<T> findByCategory(Category category);

    /**
     * @param category 数据源
     * @return 按数据源查找
     */
    List<T> findByCategory_Id(Long category);

    Page<T> findByCategory(Category category, Pageable pageable);

    /**
     * @param site 站点
     * @return 按站点查找
     */
    List<T> findByCategory_Site(Site site);

    Page<T> findByCategory_Site(Site site, Pageable pageable);

    /**
     * 按照站点和唯一序列号查找
     *
     * @param site   站点
     * @param serial 序列号
     * @return 内容
     */
    T findByCategory_SiteAndSerial(Site site, String serial);

    /**
     * 删除所有该数据源相关
     *
     * @param category 指定数据源
     * @return 删除的数量
     */
    long deleteByCategory(Category category);

}
