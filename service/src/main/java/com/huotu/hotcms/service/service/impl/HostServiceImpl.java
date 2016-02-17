package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Region;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.HostRepository;
import com.huotu.hotcms.service.service.HostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cwb on 2015/12/24.
 */
@Service
public class HostServiceImpl implements HostService {

    @Autowired
    private HostRepository hostRepository;

    @Override
    public Host getHost(String domain) {
        return hostRepository.findByDomain(domain);
    }

    @Override
    public Boolean save(Host host) {
        hostRepository.save(host);
        return true;
    }

    @Override
    public Boolean isExists(String domain,Set<Host> hostSet) {
        for (Host host:hostSet){
            if(host!=null) {
                if (host.getDomain().equals(domain)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Site mergeSite(String[] domains,Site site){
        Site site1=new Site();
       Set<Host> hostSet=site.getHosts();
        for (Host host:hostSet){
            boolean isExists=false;
            for (String domain : domains) {
                if(domain.equals(host.getDomain())){
                    isExists=true;
                }
            }
            if(isExists){
                site1.addHost(host);
            }
        }
        site.setHosts(site1.getHosts());
        return site;
    }

    @Override
    /**
     * <p>
     *     获得需要移除的Host列表
     * </p>
     * @param domains 新的域名列表
     * @param site 目标站点信息
     * @return
     * */
    public Set<Host> getRemoveHost(String[] domains, Site site) {
        Set<Host> hosts = new HashSet<>();
        Set<Host> hostSet = site.getHosts();
        for (Host host : hostSet) {
            boolean isExists = false;
            for (String domain : domains) {
                if (domain.equals(host.getDomain())) {
                    isExists = true;
                }
            }
            if (!isExists) {
                if (host.getSites() != null) {
                    if (host.getSites().size() == 0) {
                        hosts.add(host);
                    } else if (host.getSites().size() == 1) {
                        if (host.getSites().contains(site)) {
                            hosts.add(host);
                        }
                    }
                }
            }
        }
        return hosts;
    }

    @Override
    /**
     * <p>删除Host列表</p>
     * @param hostSet 要移除的Host列表
     * */
    public boolean removeHost(Set<Host> hostSet) {
        if(hostSet!=null) {
            for (Host host : hostSet) {
                if (host != null) {
                    host.setSites(null);
                    hostRepository.delete(host);
                }
            }
        }
        return true;
    }

    @Override
    public Boolean isExistsByDomains(String[] domains,Long regionId) {
        Boolean isExists=false;
        for(String domain:domains){
           Host host=getHost(domain);
            if(host!=null){
                if(host.getSites()!=null) {
                    for (Site site : host.getSites()) {
                        if (site!=null){
                            if(site.getRegion()!=null){
                                if(site.getRegion().getId().equals(regionId)){
                                    isExists=true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return isExists;
    }

    @Override
    public Boolean isNotExistsByDomainsAndSite(String[] domains, Site site,Long regionId) {
        boolean isExists=false;
        for(String domain:domains) {
            Host host = getHost(domain);
            if (host != null) {
                if (host.getSites() != null) {
                    if(host.getSites().contains(site)){
                        for (Site sites : host.getSites()) {
                            if (sites!=null){
                                if(sites.getRegion()!=null){
                                    if(sites.getRegion().getId().equals(regionId)){
                                        isExists=true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return isExists;
    }
}
