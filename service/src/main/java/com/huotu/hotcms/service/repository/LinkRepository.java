package com.huotu.hotcms.service.repository;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.model.Contents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Vector;

/**
 * Created by chendeyu on 2016/1/6.
 */
public interface  LinkRepository extends JpaRepository<Link, Long>,JpaSpecificationExecutor {
    @Query(value = "select a.title,a.description,c.name,a.id,c.modelId,a.createTime from cms_link a,cms_category c where a.categoryId=c.id and a.deleted=false and c.siteId = ?1\n" +
            "UNION\n" +
            "select b.title,b.description,c.name,b.id,c.modelId,b.createTime from cms_article b,cms_category c where b.categoryId=c.id and b.deleted=false and c.siteId = ?1\n" +
            "UNION\n" +
            "select d.title,d.content,c.name,d.id,c.modelId,d.createTime from cms_notice d,cms_category c where d.category_Id=c.id and d.deleted=false and c.siteId = ?1\n" +
            "UNION\n" +
            "select e.fileName,e.description,c.name,e.id,c.modelId,e.createTime from cms_download e,cms_category c where e.categoryId=c.id and e.deleted=false and c.siteId = ?1\n" +
            "UNION\n" +
            "select f.title,f.description,c.name,f.id,c.modelId,f.createTime from cms_video f,cms_category c where f.categoryId=c.id and f.deleted=false and c.siteId = ?1\n" +
            "UNION\n" +
            "select g.title,g.description,c.name,g.id,c.modelId,g.createTime from cms_gallery g,cms_category c where g.categoryId=c.id and g.deleted=false and c.siteId = ?1",nativeQuery = true)
    List<Object[]> findAllContents(Long siteId);
}
