/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;


/**
 * redis缓存
 * Created by cwb on 2016/3/30.
 */
@Configuration
@Profile("prod")
public class RedisConfig {

    @Autowired
    private Environment env;
    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;
    @Autowired
    private JedisPoolConfig jedisPoolConfig;

    private String hostName;
    private String password;
    private Integer port;
    private Integer maxTotal;
    private Long maxWait;
    private Integer maxIdle;
    private Integer dbIndex;

    public void init() {
        hostName = env.getProperty("redisHost","localhost");
        port = env.getProperty("redisPort",Integer.class,6379);
        password = env.getProperty("redisAuth");
        maxTotal = env.getProperty("jedisPool.maxTotal", Integer.class, 500);
        maxWait = env.getProperty("jedisPool.maxWait", Long.class, 1000l * 3);
        maxIdle = env.getProperty("jedisPool.maxIdle", Integer.class, 200);
        dbIndex = env.getProperty("redisDatabase",Integer.class,20);
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        init();
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(hostName);
        if(!StringUtils.isEmpty(password)) {
            jedisConnectionFactory.setPassword(password);
        }
        jedisConnectionFactory.setDatabase(dbIndex);
        jedisConnectionFactory.setPort(port);
        jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
        return jedisConnectionFactory;
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        jedisPoolConfig.setMaxIdle(maxIdle);
        return jedisPoolConfig;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

}
