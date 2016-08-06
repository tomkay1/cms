package com.huotu.hotcms.service.event;

import com.huotu.hotcms.service.entity.Site;
import lombok.Data;

/**
 * 删除一个站点的事件
 *
 * @author HZCJ
 */
@Data
public class DeleteSiteEvent {

    private final Site site;

}
