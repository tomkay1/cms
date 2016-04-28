package com.huotu.hotcms.service.model.thymeleaf.next;

import com.huotu.hotcms.service.model.thymeleaf.foreach.Rename;
import lombok.Getter;
import lombok.Setter;

/**
 * 文章下一条记录解析器参数模型
 * Created by Administrator xhl 2016/1/20.
 */
@Setter
@Getter
public class ArticleNextParam {
    /**
     * 当前文章ID,根据该ID来获得下一条文章
     * */
    @Rename("id")
    public Long id;
}
