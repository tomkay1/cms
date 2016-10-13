package com.huotu.hotcms.service.entity.converter;

import org.springframework.util.NumberUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lhx on 2016/10/13.
 */
@Converter
public class MallProductCategoryAttrConverter implements AttributeConverter<List<Long>, String> {
    @Override
    public String convertToDatabaseColumn(List<Long> longs) {
        return Arrays.toString(longs.toArray()).replace("[", "").replace("]", "");
    }

    @Override
    public List<Long> convertToEntityAttribute(String s) {
        if (s == null || s.equals(""))
            return null;
        else {
            String[] array = s.split(",");
            List<Long> list = new ArrayList<>();
            for (String str : array) {
                list.add(NumberUtils.parseNumber(str, Long.class));
            }
            return list;
        }
    }
}
