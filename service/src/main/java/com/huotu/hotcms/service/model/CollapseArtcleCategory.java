package com.huotu.hotcms.service.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhx on 2016/7/25.
 */
@Getter
@Setter
public class CollapseArtcleCategory {

    private Long categoryId;
    /**
     * 标题
     */
    private String text;//title

    private String href; //

    private Long parentId;

    private List<CollapseArtcleCategory> nodes = new ArrayList<>();//子模型
}
