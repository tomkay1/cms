package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by hzbc on 2016/5/18.
 */
@Repository
public interface TemplateRepository extends JpaRepository<Template,Long>,JpaSpecificationExecutor<Template>{

}
