package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.Host;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by cwb on 2015/12/24.
 */
public interface HostRepository extends JpaRepository<Host,Long> {
    Host findByDomain(String domain);

}
