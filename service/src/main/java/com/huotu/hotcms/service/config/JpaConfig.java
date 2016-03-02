package com.huotu.hotcms.service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by luffy on 2015/6/10.
 *
 * @author luffy luffy.ja at gmail.com
 */
@Configuration
@EnableJpaRepositories("com.huotu.hotcms.service.repository")
public class JpaConfig {

}
