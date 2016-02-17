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

    /**
     * <p>
     *     根据域名列表和地区ID判断域名列表中是否存在重复
     * </p>
     * @param domains 域名列表
     * @param regionId 地区ID
     * */
    Boolean isExistsByDomains(String[] domains,Long regionId);

    Boolean isNotExistsByDomainsAndSite(String[] domains,Site site,Long regionId);

    /**
     * <p>
     *     获得需要移除的Host列表
     * </p>
     * @param domains 新的域名列表
     * @param site 目标站点信息
     * @return
     * */
    Set<Host> getRemoveHost(String[] domains,Site site);

    /**
     * <p>删除Host列表</p>
     * @param hostSet 要移除的Host列表
     * */
    boolean removeHost(Set<Host> hostSet);
}
