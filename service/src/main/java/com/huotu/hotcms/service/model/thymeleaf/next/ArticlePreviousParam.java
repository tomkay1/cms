package com.huotu.hotcms.service.model.thymeleaf.next;

import lombok.Getter;
import lombok.Setter;

/**
 * 文章上一条记录解析器参数模型
 * Created by Administrator xhl 2016/1/20.
 */
@Setter
@Getter
public class ArticlePreviousParam {
    /**
     * 当前文章ID,根据该ID来获得上一条文章
     * */
    private Long id;
}
