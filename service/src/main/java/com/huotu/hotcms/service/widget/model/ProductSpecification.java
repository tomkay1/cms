/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.widget.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Set;

/**
 * @author CJ
 */
@Data
public class ProductSpecification {

    @JsonProperty("ProductId")
    private Long productId;
    /**
     * 规格
     */
    @JsonProperty("Desc")
    @JsonDeserialize(converter = ToProductSpecificationConverter.class)
    @JsonSerialize(converter = FromProductSpecificationConverter.class)
    private Set<String> specifications;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\"ProductId\":").append(productId).append(",\"Desc\":\"");
        if (specifications != null && !specifications.isEmpty()) {
            for (String temp : specifications) {
                result.append(temp).append(",");//
            }
            result.deleteCharAt(result.length() -1);
        }
        result.append("\"");
        return result.toString();
    }
}
