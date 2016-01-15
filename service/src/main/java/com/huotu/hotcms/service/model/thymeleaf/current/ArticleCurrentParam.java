package com.huotu.hotcms.service.model.thymeleaf.current;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator xhl 2016/1/15.
 */
@Setter
@Getter
public class ArticleCurrentParam {
    /**
     * 制定ID,如果没有则根据当前请求的Uri的环境获得当前所属文章ID
     * **/
    private Long id;
}
