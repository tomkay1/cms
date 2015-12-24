package com.huotu.hotcms.service;

import com.huotu.hotcms.entity.Host;
import com.huotu.hotcms.entity.Site;

/**
 * Created by cwb on 2015/12/24.
 */
public interface HostService {

    Host getSite(String domain);
}
