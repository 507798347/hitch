package com.syduck.hitchaccount.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syduck.hitchcommons.entity.SessionContext;
import com.syduck.hitchcommons.helper.RedisSessionHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/*
 * 第一个 Bean：getStringRedisTemplate，创建一个 StringRedisTemplate，用来处理字符串类型的键和值
 * 第二个 Bean：getredisSessionTemplate，创建一个 RedisTemplate<String, SessionContext>，
 *              用来处理键是字符串、值是 SessionContext 对象的 Redis 操作
 * 第三个 Bean：创建一个 RedisSessionHelper 对象，可能是用来辅助管理会话的工具类
 * */

@Configuration
public class RedisConfiguration {

    @Bean
    public StringRedisTemplate getStringRedisTemplate(RedisConnectionFactory factory){
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        //RedisConnectionFactory 是 Redis 的连接工厂，负责建立与 Redis 服务器的连接
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }

    // redis中存储的是 会话SessionContext
    @Bean(name = "redisSessionTemplate")
    public RedisTemplate<String, SessionContext> getredisSessionTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, SessionContext> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        //设置键的序列化方式为 StringRedisSerializer，确保键以字符串形式存储在 Redis 中
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        /*值序列化部分*/
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer); // value的序列化类型
        return redisTemplate;
    }

    @Bean
    public RedisSessionHelper redisSessionHelper(){
        return new RedisSessionHelper();
    }
}
