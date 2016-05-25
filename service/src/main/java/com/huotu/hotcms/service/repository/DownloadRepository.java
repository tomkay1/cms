package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Download;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by chendeyu on 2016/1/11.
 */
public interface DownloadRepository extends JpaRepository<Download, Long>,JpaSpecificationExecutor {
    /**
     * 通过类目查询 下载模型
     * @param category 类目
     * @return 下载模型
     */
    List<Download> findByCategory(Category category);
}
