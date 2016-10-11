package com.huotu.hotcms.service.model;

import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.MallClassCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by lhx on 2016/10/8.
 */
@Getter
@Setter
public class MallClassCategoryModel extends MallClassCategory {
    List<MallClassCategoryModel> children;
    List<Link> links;
}
