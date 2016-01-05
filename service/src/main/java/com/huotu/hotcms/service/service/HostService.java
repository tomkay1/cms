package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Host;

/**
 * Created by cwb on 2015/12/24.
 */
public interface HostService {

    Host getHost(String domain);

    Boolean save(Host host);

}
