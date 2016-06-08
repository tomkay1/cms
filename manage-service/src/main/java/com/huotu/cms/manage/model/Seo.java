package com.huotu.cms.manage.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by cwb on 2015/12/28.
 */
@Getter
@Setter
public class Seo {
    private String title;

    public Seo(String seo) {
        this.setTitle(seo);
    }
}
