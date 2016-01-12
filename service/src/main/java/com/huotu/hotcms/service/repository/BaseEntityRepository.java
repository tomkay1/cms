package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by chendeyu on 2016/1/12.
 */
public interface BaseEntityRepository extends JpaRepository<BaseEntity,Long>,JpaSpecificationExecutor {
    List<BaseEntity> findByCategoryId(Long categoryId);

    @Query(value = "select * from (select a.title,a.description,c.name,a.id,c.modelId,a.createTime from cms_link a,cms_category c where a.categoryId=c.id and a.deleted=false and c.siteId = ?1 and a.title like %?2%\n" +
            "UNION\n" +
            "select b.title,b.description,c.name,b.id,c.modelId,b.createTime from cms_article b,cms_category c where b.categoryId=c.id and b.deleted=false and c.siteId = ?1 and b.title like %?2%\n" +
            "UNION\n" +
            "select d.title,d.description,c.name,d.id,c.modelId,d.createTime from cms_notice d,cms_category c where d.categoryId=c.id and d.deleted=false and c.siteId = ?1 and d.title like %?2%\n" +
            "UNION\n" +
            "select e.title,e.description,c.name,e.id,c.modelId,e.createTime from cms_download e,cms_category c where e.categoryId=c.id and e.deleted=false and c.siteId = ?1 and e.title like %?2%\n" +
            "UNION\n" +
            "select f.title,f.description,c.name,f.id,c.modelId,f.createTime from cms_video f,cms_category c where f.categoryId=c.id and f.deleted=false and c.siteId = ?1 and f.title like %?2%\n" +
            "UNION\n" +
            "select g.title,g.description,c.name,g.id,c.modelId,g.createTime from cms_gallery g,cms_category c where g.categoryId=c.id and g.deleted=false and c.siteId = ?1 and g.title like %?2%) as t order by modelId,id limit ?3,?4",nativeQuery = true)
    List<Object[]> findAllContentsBySiteIdAndName(Long siteId,String name,int page, int pageSize);

    @Query(value = "select * from(select a.title,a.description,c.name,a.id,c.modelId,a.createTime from cms_link a,cms_category c where a.categoryId=c.id and a.deleted=false and c.siteId = ?1 and c.id=?2 and a.title like %?3%\n" +
            "UNION\n" +
            "select b.title,b.description,c.name,b.id,c.modelId,b.createTime from cms_article b,cms_category c where b.categoryId=c.id and b.deleted=false and c.siteId = ?1 and c.id=?2 and b.title like %?3%\n" +
            "UNION\n" +
            "select d.title,d.description,c.name,d.id,c.modelId,d.createTime from cms_notice d,cms_category c where d.categoryId=c.id and d.deleted=false and c.siteId = ?1 and c.id=?2 and d.title like %?3%\n" +
            "UNION\n" +
            "select e.title,e.description,c.name,e.id,c.modelId,e.createTime from cms_download e,cms_category c where e.categoryId=c.id and e.deleted=false and c.siteId = ?1 and c.id=?2 and e.title like %?3%\n" +
            "UNION\n" +
            "select f.title,f.description,c.name,f.id,c.modelId,f.createTime from cms_video f,cms_category c where f.categoryId=c.id and f.deleted=false and c.siteId = ?1 and c.id=?2 and f.title like %?3%\n" +
            "UNION\n" +
            "select g.title,g.description,c.name,g.id,c.modelId,g.createTime from cms_gallery g,cms_category c where g.categoryId=c.id and g.deleted=false and c.siteId = ?1 and c.id=?2 and g.title like %?3%) as t order by id,modelId limit ?4,?5",nativeQuery = true)
    List<Object[]> findAllContentsBySiteIdAndCategoryIdAndName(Long siteId,Long categoryId,String name,int page, int pageSize);


    @Query(value = "select * from (select a.title,a.description,c.name,a.id,c.modelId,a.createTime from cms_link a,cms_category c where a.categoryId=c.id and a.deleted=false and c.siteId = ?1 and a.title like %?2%\n" +
            "UNION\n" +
            "select b.title,b.description,c.name,b.id,c.modelId,b.createTime from cms_article b,cms_category c where b.categoryId=c.id and b.deleted=false and c.siteId = ?1 and b.title like %?2%\n" +
            "UNION\n" +
            "select d.title,d.description,c.name,d.id,c.modelId,d.createTime from cms_notice d,cms_category c where d.categoryId=c.id and d.deleted=false and c.siteId = ?1 and d.title like %?2%\n" +
            "UNION\n" +
            "select e.title,e.description,c.name,e.id,c.modelId,e.createTime from cms_download e,cms_category c where e.categoryId=c.id and e.deleted=false and c.siteId = ?1 and e.title like %?2%\n" +
            "UNION\n" +
            "select f.title,f.description,c.name,f.id,c.modelId,f.createTime from cms_video f,cms_category c where f.categoryId=c.id and f.deleted=false and c.siteId = ?1 and f.title like %?2%\n" +
            "UNION\n" +
            "select g.title,g.description,c.name,g.id,c.modelId,g.createTime from cms_gallery g,cms_category c where g.categoryId=c.id and g.deleted=false and c.siteId = ?1 and g.title like %?2%) as t",nativeQuery = true)
    List<Object[]> findContentsSizeBySiteIdAndName(Long siteId,String name);

    @Query(value = "select * from(select a.title,a.description,c.name,a.id,c.modelId,a.createTime from cms_link a,cms_category c where a.categoryId=c.id and a.deleted=false and c.siteId = ?1 and c.id=?2 and a.title like %?3%\n" +
            "UNION\n" +
            "select b.title,b.description,c.name,b.id,c.modelId,b.createTime from cms_article b,cms_category c where b.categoryId=c.id and b.deleted=false and c.siteId = ?1 and c.id=?2 and b.title like %?3%\n" +
            "UNION\n" +
            "select d.title,d.description,c.name,d.id,c.modelId,d.createTime from cms_notice d,cms_category c where d.categoryId=c.id and d.deleted=false and c.siteId = ?1 and c.id=?2 and d.title like %?3%\n" +
            "UNION\n" +
            "select e.title,e.description,c.name,e.id,c.modelId,e.createTime from cms_download e,cms_category c where e.categoryId=c.id and e.deleted=false and c.siteId = ?1 and c.id=?2 and e.title like %?3%\n" +
            "UNION\n" +
            "select f.title,f.description,c.name,f.id,c.modelId,f.createTime from cms_video f,cms_category c where f.categoryId=c.id and f.deleted=false and c.siteId = ?1 and c.id=?2 and f.title like %?3%\n" +
            "UNION\n" +
            "select g.title,g.description,c.name,g.id,c.modelId,g.createTime from cms_gallery g,cms_category c where g.categoryId=c.id and g.deleted=false and c.siteId = ?1 and c.id=?2 and g.title like %?3%) as t",nativeQuery = true)
    List<Object[]> findContentsSizeBySiteIdAndCategoryIdAndName(Long siteId,Long categoryId,String name);
}
