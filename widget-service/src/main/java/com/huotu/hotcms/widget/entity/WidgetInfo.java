package com.huotu.hotcms.widget.entity;

import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetStyle;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.Resource;

import javax.persistence.*;
import java.util.Locale;

/**
 * Created by elvis on 2016/6/7.
 */
@Entity
@Table(name = "cms_widgetInfo")
@Getter
@Setter
public class WidgetInfo implements Widget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "groupId")
    private String groupId;

    @Column(name = "widgetId")
    private String widgetId;

    @Column(name = "version")
    private String version;

    @Column(name = "author")
    private String author;

    @Column(name = "dependBuild")
    private String dependBuild;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Override
    public String groupId() {
        return getGroupId();
    }

    @Override
    public String widgetId() {
        return getWidgetId();
    }

    @Override
    public String version() {
        return getVersion();
    }

    @Override
    public String author() {
        return getAuthor();
    }

    @Override
    public int dependBuild() {
        return Integer.parseInt(getDependBuild());
    }

    @Override
    public String name() {
        return getName();
    }

    @Override
    public String name(Locale locale) {
        return null;
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public String description(Locale locale) {
        return null;
    }

    @Override
    public Resource thumbnail() {
        return null;
    }

    @Override
    public Resource editorTemplate() {
        return null;
    }

    @Override
    public WidgetStyle[] styles() {
        return new WidgetStyle[0];
    }

    @Override
    public void valid(String styleId, ComponentProperties properties) throws IllegalArgumentException {

    }

    @Override
    public Class springConfigClass() {
        return null;
    }
}
