package com.huotu.hotcms.repository;

import com.huotu.hotcms.entity.Host;
import com.huotu.hotcms.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by cwb on 2015/12/24.
 */
public interface HostRepository extends JpaRepository<Host,Long> {
    Host findByDomain(String domain);
}
