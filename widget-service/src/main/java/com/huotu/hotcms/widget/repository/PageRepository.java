package com.huotu.hotcms.widget.repository;

import com.huotu.hotcms.widget.page.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by hzbc on 2016/6/22.
 */

@Repository
public interface PageRepository extends JpaRepository<Page,String>,JpaSpecificationExecutor<Page>{

}
