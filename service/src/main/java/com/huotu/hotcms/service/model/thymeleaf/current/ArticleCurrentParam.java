package com.huotu.hotcms.service.model.thymeleaf.current;

import com.huotu.hotcms.service.model.thymeleaf.foreach.Rename;
import lombok.Getter;
import lombok.Setter;

/**
 * 文章当前记录解析器参数模型
 * Created by Administrator xhl 2016/1/15.
 */
@Setter
@Getter
public class ArticleCurrentParam {
    /**
     * 文章ID,如果没有则根据当前请求的Uri的环境获得当前所属文章ID
     * **/
    @Rename("id")
    public Long id;

    /**
     * 文章默认ID,优先级最后,如果文章指定ID不为空则取默认
     * **/
    @Rename("defaultid")
    public Long defaultid;
}
