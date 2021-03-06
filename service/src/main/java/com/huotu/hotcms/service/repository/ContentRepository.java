/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.AbstractContent;
import com.huotu.hotcms.service.repositoryi.AbstractContentRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContentRepository extends AbstractContentRepository<AbstractContent> {
    /**
     * 根据站点查询所有contents
     */
    @Query(value = "select * from (select a.title,a.description,c.name,a.id,c.modelId,a.createTime from cms_link a,cms_category c where a.categoryId=c.id and a.deleted=false and c.siteId = ?1 and c.deleted=false and a.title like %?2%\n" +
            "UNION\n" +
            "select b.title,b.description,c.name,b.id,c.modelId,b.createTime from cms_article b,cms_category c where b.categoryId=c.id and b.deleted=false and c.siteId = ?1 and c.deleted=false and b.title like %?2%\n" +
            "UNION\n" +
            "select d.title,d.description,c.name,d.id,c.modelId,d.createTime from cms_notice d,cms_category c where d.categoryId=c.id and d.deleted=false and c.siteId = ?1  and c.deleted=false and d.title like %?2%\n" +
            "UNION\n" +
            "select e.title,e.description,c.name,e.id,c.modelId,e.createTime from cms_download e,cms_category c where e.categoryId=c.id and e.deleted=false and c.siteId = ?1 and c.deleted=false and e.title like %?2%\n" +
            "UNION\n" +
            "select f.title,f.description,c.name,f.id,c.modelId,f.createTime from cms_video f,cms_category c where f.categoryId=c.id and f.deleted=false and c.siteId = ?1 and c.deleted=false and f.title like %?2%\n" +
            "UNION\n" +
            "select g.title,g.description,c.name,g.id,c.modelId,g.createTime from cms_gallery g,cms_category c where g.categoryId=c.id and g.deleted=false and c.siteId = ?1 and c.deleted=false and g.title like %?2%) as t order by modelId,id limit ?3,?4", nativeQuery = true)
    List<Object[]> findAllContentsBySiteIdAndName(Long siteId, String name, int page, int pageSize);


    /**
     * 根据站点以及contents name查询所有contents,用于查总size
     */
    @Query(value = "select * from (select a.title,a.description,c.name,a.id,c.modelId,a.createTime from cms_link a,cms_category c where a.categoryId=c.id and a.deleted=false and c.deleted=false and c.siteId = ?1 and a.title like %?2%\n" +
            "UNION\n" +
            "select b.title,b.description,c.name,b.id,c.modelId,b.createTime from cms_article b,cms_category c where b.categoryId=c.id and b.deleted=false and c.deleted=false and c.siteId = ?1 and b.title like %?2%\n" +
            "UNION\n" +
            "select d.title,d.description,c.name,d.id,c.modelId,d.createTime from cms_notice d,cms_category c where d.categoryId=c.id and d.deleted=false and c.deleted=false and c.siteId = ?1 and d.title like %?2%\n" +
            "UNION\n" +
            "select e.title,e.description,c.name,e.id,c.modelId,e.createTime from cms_download e,cms_category c where e.categoryId=c.id and e.deleted=false and c.deleted=false and c.siteId = ?1 and e.title like %?2%\n" +
            "UNION\n" +
            "select f.title,f.description,c.name,f.id,c.modelId,f.createTime from cms_video f,cms_category c where f.categoryId=c.id and f.deleted=false and c.deleted=false and c.siteId = ?1 and f.title like %?2%\n" +
            "UNION\n" +
            "select g.title,g.description,c.name,g.id,c.modelId,g.createTime from cms_gallery g,cms_category c where g.categoryId=c.id and g.deleted=false and c.deleted=false and c.siteId = ?1 and g.title like %?2%) as t", nativeQuery = true)
    List<Object[]> findContentsSizeBySiteIdAndName(Long siteId, String name);

    /**
     * 根据站点和栏目以及contents name查询所有contents
     */
    @Query(value = "select * from(select a.title,a.description,c.name,a.id,c.modelId,a.createTime from cms_link a left join cms_category c on a.categoryId=c.id where  a.deleted=false and c.deleted=false and c.siteId = ?1 and c.id in(?2) and a.title like %?3%\n" +
            "UNION\n" +
            "select b.title,b.description,c.name,b.id,c.modelId,b.createTime from cms_article b left join cms_category c on b.categoryId=c.id where b.deleted=false and c.siteId = ?1 and c.deleted=false and c.id in(?2) and b.title like %?3%\n" +
            "UNION\n" +
            "select d.title,d.description,c.name,d.id,c.modelId,d.createTime from cms_notice d left join cms_category c on d.categoryId=c.id where d.deleted=false and c.siteId = ?1 and c.deleted=false and c.id in(?2) and d.title like %?3%\n" +
            "UNION\n" +
            "select e.title,e.description,c.name,e.id,c.modelId,e.createTime from cms_download e left join cms_category c on e.categoryId=c.id where e.deleted=false and c.siteId = ?1 and c.deleted=false and c.id in(?2) and e.title like %?3%\n" +
            "UNION\n" +
            "select f.title,f.description,c.name,f.id,c.modelId,f.createTime from cms_video f left join cms_category c on f.categoryId=c.id  where f.deleted=false and c.siteId = ?1 and c.deleted=false and c.id in(?2) and f.title like %?3%\n" +
            "UNION\n" +
            "select g.title,g.description,c.name,g.id,c.modelId,g.createTime from cms_gallery g left join cms_category c on g.categoryId=c.id where g.deleted=false and c.siteId = ?1 and c.deleted=false and c.id in(?2) and g.title like %?3%) as t order by id,modelId limit ?4,?5", nativeQuery = true)
    List<Object[]> findAllContentsBySiteIdAndCategoryIdsAndName(Long siteId, String categoryId, String name, int page, int pageSize);

    /**
     * 根据站点,栏目以及contents name查询所有contents,用于查总size
     */
    @Query(value = "select * from(select a.title,a.description,c.name,a.id,c.modelId,a.createTime from cms_link a left join cms_category c on a.categoryId=c.id where a.deleted=false and c.siteId = ?1 and c.id in(?2) and a.title like %?3%\n" +
            "UNION\n" +
            "select b.title,b.description,c.name,b.id,c.modelId,b.createTime from cms_article b left join cms_category c on b.categoryId=c.id where b.deleted=false and c.siteId = ?1 and c.deleted=false and c.id in(?2) and b.title like %?3%\n" +
            "UNION\n" +
            "select d.title,d.description,c.name,d.id,c.modelId,d.createTime from cms_notice d left join cms_category c on d.categoryId=c.id where d.deleted=false and c.siteId = ?1 and c.deleted=false and c.id in(?2) and d.title like %?3%\n" +
            "UNION\n" +
            "select e.title,e.description,c.name,e.id,c.modelId,e.createTime from cms_download e left join cms_category c on e.categoryId=c.id where e.deleted=false and c.siteId = ?1 and c.deleted=false and c.id in(?2) and e.title like %?3%\n" +
            "UNION\n" +
            "select f.title,f.description,c.name,f.id,c.modelId,f.createTime from cms_video f left join cms_category c on f.categoryId=c.id where f.deleted=false and c.siteId = ?1 and c.deleted=false and c.id in(?2) and f.title like %?3%\n" +
            "UNION\n" +
            "select g.title,g.description,c.name,g.id,c.modelId,g.createTime from cms_gallery g left join cms_category c on g.categoryId=c.id where g.deleted=false and c.siteId = ?1 and c.deleted=false and c.id in(?2) and g.title like %?3%) as t", nativeQuery = true)
    List<Object[]> findContentsSizeBySiteIdAndCategoryIdsAndName(Long siteId, String categoryId, String name);

}
