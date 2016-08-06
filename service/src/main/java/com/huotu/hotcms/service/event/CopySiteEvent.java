package com.huotu.hotcms.service.event;

import com.huotu.hotcms.service.entity.Site;
import lombok.Data;

/**
 * 站点复制事件
 *
 * @author HZCJ
 */
@Data
public class CopySiteEvent {

    private final Site src;
    private final Site dist;

}
