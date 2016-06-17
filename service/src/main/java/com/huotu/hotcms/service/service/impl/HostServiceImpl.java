/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.HostRepository;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.service.service.HostService;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import com.huotu.hotcms.service.util.SerialUtil;
import com.huotu.hotcms.service.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by cwb on 2015/12/24.
 */
@Service
public class HostServiceImpl implements HostService {

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private SiteRepository siteRepository;

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
                    isExists = isExists(regionId, isExists, host);
                }
            }
        }
        return isExists;
    }

    @Override
    public Boolean isExistsByDomainsAndSite(String[] domains, Site site,Long regionId) {
        boolean isExists=false;
        for(String domain:domains) {
            Host host = getHost(domain);
            if (host != null) {
                if (host.getSites() != null) {
                    if(host.getSites().contains(site)){
                        isExists = isExists(regionId, isExists, host);
                    }
                }
            }
        }
        return isExists;
    }

    private boolean isExists(Long regionId, boolean isExists, Host host) {
        for (Site sites : host.getSites()) {
            if (sites != null) {
                if (sites.getRegion() != null) {
                    if (sites.getRegion().getId().equals(regionId)) {
                        isExists = true;
                    }
                }
            }
        }
        return isExists;
    }


    @Override
    public ResultView addHost(String[] domains,String homeDomains, Site site,Long regionId) {
        ResultView result = null;
        if (!StringUtil.Contains(domains, homeDomains)) {//不存在主推域名则外抛出
            return new ResultView(ResultOptionEnum.NOFIND_HOME_DEMON.getCode(), ResultOptionEnum.NOFIND_HOME_DEMON.getValue(), null);
        }
        Long siteId = site.getSiteId();
        if (siteId == null) {//新增站点
            if (isExistsByDomains(domains, regionId)) {
                return new ResultView(ResultOptionEnum.DOMAIN_EXIST.getCode(), ResultOptionEnum.DOMAIN_EXIST.getValue(), null);
            }
            for (String domain : domains) {
                Host host = getHost(domain);
                if (host == null) {//全新域名
                    host = new Host();
                    host.setOwner(site.getOwner());
                    host.setDomain(domain);
                    host.setSerial(SerialUtil.formartSerial(site));
                    host=setHome(host,homeDomains);
                    site.addHost(host);
                } else {
                    if (host.getOwner().equals(site.getOwner())) {//如果是同一商户
                        List<Site> siteList = siteRepository.findByHosts(host);
                        List<Long> regionList = new ArrayList<>();
                        for (Site site1 : siteList) {
                            regionList.add(site1.getRegion().getId());//取得包含该域名下的所有地区列表
                        }
                        if (regionList.contains(regionId)) {
                            return new ResultView(ResultOptionEnum.DOMAIN_EXIST.getCode(), ResultOptionEnum.DOMAIN_EXIST.getValue(), null);
                        } else {
                            host = setHome(host, homeDomains);
                            site.addHost(host);
                        }
                    } else {//域名不是同一个商户则返回
                        return new ResultView(ResultOptionEnum.DOMAIN_EXIST.getCode(), ResultOptionEnum.DOMAIN_EXIST.getValue(), null);
                    }
                }
            }
        }
        return new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), site);
    }

    @Override
    public ResultView patchHost(String[] domains, String homeDomains, Site site, Long regionId) {
        Set<Host> hosts=new HashSet<>();
        if (isExistsByDomainsAndSite(domains, site, regionId)) {
            return new ResultView(ResultOptionEnum.DOMAIN_EXIST.getCode(), ResultOptionEnum.DOMAIN_EXIST.getValue(),null);
        }
        for (String domain : domains) {
            if (!isExists(domain, site.getHosts())) {//不包含该域名则做下一步的判断工作
                Host host =getHost(domain);
                if (host == null) {//全新域名
                    host = new Host();
                    host.setOwner(site.getOwner());
                    host.setDomain(domain);
                    host=setHome(host,homeDomains);
                    hosts.add(host);
//                    site.addHost(host);
                } else {//不是全新域名
                    if (host.getOwner().equals(site.getOwner())) {
                        host=setHome(host,homeDomains);
                        hosts.add(host);
//                        site.addHost(host);
                    }
                    else {
                        return new ResultView(ResultOptionEnum.DOMAIN_EXIST.getCode(), ResultOptionEnum.DOMAIN_EXIST.getValue(), null);
                    }
                }
            }else{//修改主推域名
                Host host =getHost(domain);
                host=setHome(host,homeDomains);
//                hostRepository.save(host);
                hosts.add(host);
            }
        }
        site.setHosts(hosts);
        return new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), site);
    }

    private Host setHome(Host host,String homeDomains){
        if(homeDomains.equals(host.getDomain())){//设置为主域名
            host.setHome(true);
        }else{
            host.setHome(false);
        }
        return host;
    }

    @Override
    public Host getHomeHost(Site site) {
        for (Host host:site.getHosts()){
            if (host.isHome()) {
                return host;
            }
        }
        return null;
    }

    @Override
    public String getHomeDomain(Site site) {
       Host host=getHomeHost(site);
        if(host!=null){
            return host.getDomain();
        }
        return null;
    }
}
