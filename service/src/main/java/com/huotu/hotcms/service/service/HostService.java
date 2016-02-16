package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Site;

import java.util.Set;

/**
 * Created by cwb on 2015/12/24.
 */
public interface HostService {

    Host getHost(String domain);

    Boolean save(Host host);

    Boolean isExists(String domain,Set<Host> hostSet);

    Site mergeSite(String[] domains,Site site);

    Boolean isExistsByDomains(String[] domains,Long regionId);

    Boolean isNotExistsByDomainsAndSite(String[] domains,Site site,Long regionId);
}
