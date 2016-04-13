package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Region;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.util.ResultView;

import java.util.List;
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

    /**
     * <p>
     *     根据域名列表、站点信息、地区ID判断域名列表中新添加的域名是否存在重复值
     * </p>
     * @param domains 域名列表
     * @param regionId 地区ID
     * */
    Boolean isExistsByDomainsAndSite(String[] domains,Site site,Long regionId);

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

    /**
     * <p>
     *  * 站点新增域名
     * </p>
     * @param domains 要新增的域名列表
     * @param homeDomains 主推域名
     * @param site 站点对象
     * @param regionId 地区ID
     * @return ResultView 对象
     * */
    ResultView addHost(String[] domains,String homeDomains,Site site,Long regionId);

    ResultView patchHost(String[] domains,String homeDomains,Site site,Long regionId);

    Host getHomeHost(Site site);

    String getHomeDomain(Site site);
}
