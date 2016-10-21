package com.huotu.hotcms.service.model;

import com.huotu.hotcms.service.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by lhx on 2016/10/18.
 */
@Getter
@Setter
@AllArgsConstructor
public class CategoryAndContent<T> {
    private Category category;
    private List<T> list;

}
