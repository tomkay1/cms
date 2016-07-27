/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    /**
     * 根据站点查询数据源
     * @param site 站点
     * @return
     */
    List<Category> findBySite(Site site);

    /**
     * 删除相应站点下的数据源
     * @param site
     * @return
     */
    Long deleteBySite(Site site);

    /**
     * 根据序列号和站点信息进行查询
     *
     * @param serial 序列号
     * @param site   站点 {@link com.huotu.hotcms.service.entity.Site}
     * @return category {@link com.huotu.hotcms.service.entity.Category}
     */
    Category findBySerialAndSite(String serial, Site site);

    /**
     * 获取某站点下的所有栏目
     *
     * @param site 站点
     * @return 所有栏目
     */
    List<Category> findBySiteOrderByOrderWeightDesc(Site site);

    /**
     * 根据站点查询栏目
     */
    List<Category> findBySiteAndDeletedOrderByOrderWeightDesc(Site site, Boolean deleted);

    /**
     * 根据站点,以及栏目名查询栏目
     */
    List<Category> findBySiteAndDeletedAndNameContainingOrderByOrderWeightDesc(Site site, Boolean deleted, String name);

    /**
     * 根据所有者,站点查询栏目
     */
    List<Category> findBySite_Owner_IdAndSite_SiteIdAndDeletedAndContentTypeNotNullOrderByOrderWeightDesc(long ownerId
            , Long siteId, boolean deleted);

    /**
     * 根据所有者,模型,以及栏目名查询栏目
     */
    Set<Category> findBySite_Owner_IdAndContentType(long ownerId, ContentType contentType);

    /**
     * 根据站点查询栏目
     */
    List<Category> findBySiteAndDeletedAndContentTypeNotNullOrderByOrderWeightDesc(Site site, boolean deleted);

    /**
     * 根据父栏目查询栏目
     */
    List<Category> findByParentOrderByOrderWeightDesc(Category superCategory);

    /**
     * 根据父栏目查询栏目
     */
    List<Category> findByParent_Id(Long parenId);

    List<Category> findBySiteAndContentType(Site site, ContentType contentType);




}
