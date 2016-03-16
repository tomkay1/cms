package com.huotu.hotcms.service.common;

/**
 * <p>
 *     页面布局枚举
 * </p>
 *
 * @since 1.2
 *
 * @author xhl
 */
public enum LayoutEnum implements CommonEnum {

    THREE_COLUMN_LAYOUT_190x590x190(0,"三栏布局(190x590x190)"),
    WITHOUT_COLUMN_LAYOUT_990(1,"通栏布局(990)"),
    LEFT_RIGHT_COLUMN_LAYOUT_190x790(2,"左右栏布局（190x790)"),
    RIGHT_PART_LAYOUT_190x390x390(3,"右等分布局(190x390x390)"),
    LEFT_RIGHT_COLUMN_LAYOUT_790x190(4," 左右栏布局(790x190)"),
    LEFT_PART_LAYOUT_390x390x190(5,"左等分布局（390x390x190）"),
    THREE_COLUMN_LAYOUT_254x717x239(6,"三栏布局(254x717x239)"),
    LEFT_RIGHT_COLUMN_LAYOUT_254x956(7,"左右布局（254x956）"),
    LEFT_RIGHT_COLUMN_LAYOUT_272x718(8,"左右栏布局（272x718）"),
    LEFT_RIGHT_COLUMN_LAYOUT_215x765(9,"左右布局（215x765）"),
    LEFT_RIGHT_COLUMN_LAYOUT_330x650(10,"左右栏布局(330x650)"),
    LEFT_RIGHT_COLUMN_LAYOUT_650x330(11,"左右栏布局(650x330)"),
    LEFT_RIGHT_PART_LAYOUT_490x490(12,"左右等分布局（490x490）"),
    LEFT_CENTER_RIGHT_PART_LAYOUT_323x324x323(13,"左中右等分布局(323x324x323)"),
    WITHOUT_COLUMN_LAYOUT_99999(14,"通栏布局(100%)");


    LayoutEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    private int code;
    private String value;

    @Override
    public Object getCode() {
        return this.code;
    }

    @Override
    public Object getValue() {
        return this.value;
    }
}
