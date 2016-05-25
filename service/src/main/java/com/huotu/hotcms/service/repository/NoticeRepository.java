package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by chendeyu on 2016/1/5.
 */
public interface NoticeRepository extends JpaRepository<Notice, Long>,JpaSpecificationExecutor {
    List<Notice> findByCategory(Category category);
}
