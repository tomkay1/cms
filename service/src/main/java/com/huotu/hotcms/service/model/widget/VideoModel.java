package com.huotu.hotcms.service.model.widget;

import com.huotu.hotcms.service.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by lhx on 2016/8/17.
 */
@Getter
@Setter
public class VideoModel extends BaseModel {
    private String thumbUri;
    /**
     * 内部储存地址,path;如果为null就表示没有保存在我方资源系统,只有{@link #outLinkUrl}可用
     */
    private String videoUrl;
    /**
     * 外部链接地址
     */
    private String outLinkUrl;
}
