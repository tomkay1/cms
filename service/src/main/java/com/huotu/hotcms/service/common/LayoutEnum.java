package com.huotu.hotcms.service.common;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 页面布局枚举
 * </p>
 *
 * @author xhl
 * @since 1.2
 */
public enum LayoutEnum implements CommonEnum {
    THREE_COLUMN_LAYOUT_190x590x190(0, "三栏布局(190x590x190)"),
    WITHOUT_COLUMN_LAYOUT_990(1, "通栏布局(990)"),
    LEFT_RIGHT_COLUMN_LAYOUT_190x790(2, "左右栏布局（190x790)"),
    RIGHT_PART_LAYOUT_190x390x390(3, "右等分布局(190x390x390)"),
    LEFT_RIGHT_COLUMN_LAYOUT_790x190(4, " 左右栏布局(790x190)"),
    LEFT_PART_LAYOUT_390x390x190(5, "左等分布局（390x390x190）"),
    THREE_COLUMN_LAYOUT_254x717x239(6, "三栏布局(254x717x239)"),
    LEFT_RIGHT_COLUMN_LAYOUT_254x956(7, "左右布局（254x956）"),
    LEFT_RIGHT_COLUMN_LAYOUT_272x718(8, "左右栏布局（272x718）"),
    LEFT_RIGHT_COLUMN_LAYOUT_215x765(9, "左右布局（215x765）"),
    LEFT_RIGHT_COLUMN_LAYOUT_330x650(10, "左右栏布局(330x650)"),
    LEFT_RIGHT_COLUMN_LAYOUT_650x330(11, "左右栏布局(650x330)"),
    LEFT_RIGHT_PART_LAYOUT_490x490(12, "左右等分布局（490x490）"),
    LEFT_CENTER_RIGHT_PART_LAYOUT_323x324x323(13, "左中右等分布局(323x324x323)"),
    WITHOUT_COLUMN_LAYOUT_99999(14, "通栏布局(100%)");

    LayoutEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    private int code;
    private String value;

    public final String ADD_TEMPLATE = "<a href=\"javascript:;\" class=\"link-add HOT-module-add js-module-add\">添加模块</a>";

    @Override
    public final Integer getCode() {
        return this.code;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    /**
     * 获得布局模版
     */
    public String getLayoutTemplate() {
        String layoutTemplate = "";
        switch (this.code) {
            case 0:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\" >\n" +
                        "      <div class=\"layout-three-left w190\" name=\"left\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-three-middle w590\" name=\"middle\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-three-right w190\" name=\"right\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                break;
            case 1:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\">\n" +
                        "      <div class=\"layout-one w990\" name=\"main\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                break;
            case 2:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\" >\n" +
                        "      <div class=\"layout-two-left w190\" name=\"left\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-two-right w790 \" name=\"right-extra\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                break;
            case 3:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\">\n" +
                        "      <div class=\"layout-three-left w190\" name=\"left\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-three-middle w390\" name=\"middle-m\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-three-right w390\" name=\"middle-r\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                break;
            case 4:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\">\n" +
                        "      <div class=\"layout-two-left w790\" name=\"left\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-two-right w190\" name=\"right-extra\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                break;
            case 5:
                layoutTemplate = "<div class=\"layouttarea HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\">\n" +
                        "      <div class=\"layout-three-left w390\" name=\"left\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-three-middle w390\" name=\"middle-m\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-three-right w190\" name=\"middle-r\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                break;
            case 6:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\">\n" +
                        "      <div class=\"layout-three-left\" name=\"left\" style=\"width:254px\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-three-middle\" name=\"middle\" style=\"width:717px;margin-left:0\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-three-right\" name=\"right\" style=\"width:239px;margin-left:0\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                break;
            case 7:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\">\n" +
                        "      <div class=\"layout-two-left\" name=\"left\" style=\"width:254px;margin:0\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-two-right\" name=\"right-extra\" style=\"width:956px;margin:0\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                break;
            case 8:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\">\n" +
                        "      <div class=\"layout-two-left\" name=\"left\" style=\"width:272px;margin:0\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-two-right\" name=\"right-extra\" style=\"width:718px;margin:0\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                break;
            case 9:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\">\n" +
                        "      <div class=\"layout-two-left w215\" name=\"left\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-two-right w765\" name=\"right\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                break;
            case 10:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\">\n" +
                        "      <div class=\"layout-two-left\" style=\"width:330px;\" name=\"left\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-two-right\" style=\"width:650px;\" name=\"right-extra\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                break;
            case 11:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\">\n" +
                        "      <div class=\"layout-two-left\" style=\"width:650px;\" name=\"left\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-two-right\" style=\"width:330px;\" name=\"right-extra\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                break;
            case 12:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\">\n" +
                        "      <div class=\"layout-two-left w490\" name=\"left\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-two-right w490\" name=\"right-extra\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                break;
            case 13:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\">\n" +
                        "      <div class=\"layout-three-left w323\" name=\"left\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-three-middle w324\" name=\"middle-m\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-three-right w323\" name=\"middle-r\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                break;
            case 14:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout layout-auto HOT-layout\">\n" +
                        "      <div class=\"layout-one layout-hover\" name=\"main\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                break;
        }
        return layoutTemplate;
    }

    public String getLayoutTemplate(List<String> argument) {
        if(argument==null){
            argument=new ArrayList<>();
            argument.add(this.ADD_TEMPLATE);
            argument.add(this.ADD_TEMPLATE);
            argument.add(this.ADD_TEMPLATE);
        }
        String layoutTemplate = "";
        switch (this.code) {
            case 0:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\" >\n" +
                        "      <div class=\"layout-three-left w190\" name=\"left\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-three-middle w590\" name=\"middle\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-three-right w190\" name=\"right\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                layoutTemplate = String.format(layoutTemplate, argument.get(0), argument.get(1), argument.get(2));
                break;
            case 1:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\">\n" +
                        "      <div class=\"layout-one w990\" name=\"main\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                layoutTemplate = String.format(layoutTemplate, argument.get(0));
                break;
            case 2:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\" >\n" +
                        "      <div class=\"layout-two-left w190\" name=\"left\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-two-right w790 \" name=\"right-extra\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                layoutTemplate = String.format(layoutTemplate, argument.get(0), argument.get(1));
                break;
            case 3:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\">\n" +
                        "      <div class=\"layout-three-left w190\" name=\"left\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-three-middle w390\" name=\"middle-m\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-three-right w390\" name=\"middle-r\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                layoutTemplate = String.format(layoutTemplate, argument.get(0), argument.get(1), argument.get(2));
                break;
            case 4:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\">\n" +
                        "      <div class=\"layout-two-left w790\" name=\"left\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-two-right w190\" name=\"right-extra\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                layoutTemplate = String.format(layoutTemplate, argument.get(0), argument.get(1));
                break;
            case 5:
                layoutTemplate = "<div class=\"layouttarea HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\">\n" +
                        "      <div class=\"layout-three-left w390\" name=\"left\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-three-middle w390\" name=\"middle-m\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-three-right w190\" name=\"middle-r\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                layoutTemplate = String.format(layoutTemplate, argument.get(0), argument.get(1), argument.get(2));
                break;
            case 6:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\">\n" +
                        "      <div class=\"layout-three-left\" name=\"left\" style=\"width:254px\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-three-middle\" name=\"middle\" style=\"width:717px;margin-left:0\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-three-right\" name=\"right\" style=\"width:239px;margin-left:0\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                layoutTemplate = String.format(layoutTemplate, argument.get(0), argument.get(1), argument.get(2));
                break;
            case 7:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\">\n" +
                        "      <div class=\"layout-two-left\" name=\"left\" style=\"width:254px;margin:0\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-two-right\" name=\"right-extra\" style=\"width:956px;margin:0\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                layoutTemplate = String.format(layoutTemplate, argument.get(0), argument.get(1));
                break;
            case 8:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\">\n" +
                        "      <div class=\"layout-two-left\" name=\"left\" style=\"width:272px;margin:0\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-two-right\" name=\"right-extra\" style=\"width:718px;margin:0\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                layoutTemplate = String.format(layoutTemplate, argument.get(0), argument.get(1));
                break;
            case 9:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\">\n" +
                        "      <div class=\"layout-two-left w215\" name=\"left\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-two-right w765\" name=\"right\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                layoutTemplate = String.format(layoutTemplate, argument.get(0), argument.get(1));
                break;
            case 10:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\">\n" +
                        "      <div class=\"layout-two-left\" style=\"width:330px;\" name=\"left\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-two-right\" style=\"width:650px;\" name=\"right-extra\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                layoutTemplate = String.format(layoutTemplate, argument.get(0), argument.get(1));
                break;
            case 11:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\">\n" +
                        "      <div class=\"layout-two-left\" style=\"width:650px;\" name=\"left\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-two-right\" style=\"width:330px;\" name=\"right-extra\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                layoutTemplate = String.format(layoutTemplate, argument.get(0), argument.get(1));
                break;
            case 12:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\">\n" +
                        "      <div class=\"layout-two-left w490\" name=\"left\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-two-right w490\" name=\"right-extra\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                layoutTemplate = String.format(layoutTemplate, argument.get(0), argument.get(1));
                break;
            case 13:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout HOT-layout\">\n" +
                        "      <div class=\"layout-three-left w323\" name=\"left\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-three-middle w324\" name=\"middle-m\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "      <div class=\"layout-three-right w323\" name=\"middle-r\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                layoutTemplate = String.format(layoutTemplate, argument.get(0), argument.get(1), argument.get(2));
                break;
            case 14:
                layoutTemplate = "<div class=\"layout-area HOT-layout-area js-layout\">\n" +
                        "    <div class=\"layout layout-auto HOT-layout\">\n" +
                        "      <div class=\"layout-one layout-hover\" name=\"main\">\n" +
                        "       <div class=\"layout-tool HOT-tool\">%s" +
                        "        </div>" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </div>";
                layoutTemplate = String.format(layoutTemplate, argument.get(0));
                break;
        }
        return layoutTemplate;
    }

    public static LayoutEnum valueOf(int id) {
        switch (id) {
            case 0:
                return THREE_COLUMN_LAYOUT_190x590x190;
            case 1:
                return WITHOUT_COLUMN_LAYOUT_990;
            case 2:
                return LEFT_RIGHT_COLUMN_LAYOUT_190x790;
            case 3:
                return RIGHT_PART_LAYOUT_190x390x390;
            case 4:
                return LEFT_RIGHT_COLUMN_LAYOUT_790x190;
            case 5:
                return LEFT_PART_LAYOUT_390x390x190;
            case 6:
                return THREE_COLUMN_LAYOUT_254x717x239;
            case 7:
                return LEFT_RIGHT_COLUMN_LAYOUT_254x956;
            case 8:
                return LEFT_RIGHT_COLUMN_LAYOUT_272x718;
            case 9:
                return LEFT_RIGHT_COLUMN_LAYOUT_215x765;
            case 10:
                return LEFT_RIGHT_COLUMN_LAYOUT_330x650;
            case 11:
                return LEFT_RIGHT_COLUMN_LAYOUT_650x330;
            case 12:
                return LEFT_RIGHT_PART_LAYOUT_490x490;
            case 13:
                return LEFT_CENTER_RIGHT_PART_LAYOUT_323x324x323;
            case 14:
                return WITHOUT_COLUMN_LAYOUT_99999;
            default:
                return null;
        }
    }

}
