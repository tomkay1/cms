package com.huotu.hotcms.service.repository;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by chendeyu on 2016/1/6.
 */
public interface  LinkRepository extends JpaRepository<Link, Long>,JpaSpecificationExecutor {
    List<Link> findByIdInAndDeletedOrderByOrderWeight(List<Long> specifyIds, boolean b);

    List<Link> findByCategory(Category category);
}
