/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.widget.test;

import me.jiangcai.lib.test.config.H2DataSourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * 总比mysql快吧
 *
 * @author CJ
 */
public class DSConfig extends H2DataSourceConfig {

    @Bean
    @Primary
    public DataSource dataSource() throws IOException {
        return dataSource("cms");
    }

}
