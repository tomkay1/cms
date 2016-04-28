package com.huotu.hotcms.service.model.thymeleaf.current;

import com.huotu.hotcms.service.model.thymeleaf.foreach.Rename;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.ReadOnlyProperty;

/**
 * 视频当前记录解析器参数模型
 * Created by Administrator on 2016/1/18.
 */
@Setter
@Getter
public class VideoCurrentParam {
    /**
     * 视频ID,如果没有则根据当前请求的Uri的环境获得当前所属视频ID
     * **/
    @Rename("id")
    public Long id;

    /**
     * 视频默认ID,优先级最后,如果视频指定ID不为空则取默认
     * **/
    @Rename("defaultid")
    public Long defaultid;
}
