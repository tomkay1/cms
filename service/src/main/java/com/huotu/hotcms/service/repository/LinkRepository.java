package com.huotu.hotcms.service.repository;
import com.huotu.hotcms.service.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by chendeyu on 2016/1/6.
 */
public interface  LinkRepository extends JpaRepository<Link, Long>,JpaSpecificationExecutor {
}
