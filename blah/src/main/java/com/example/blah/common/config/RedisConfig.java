package com.example.blah.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@PropertySource("classpath:redis.properties")
public class RedisConfig {
	
	@Value("${spring.data.redis.host}")
	private String host;
	
	@Value("${spring.data.redis.port}")
	private int port;
	
	// 프로퍼티스에서 설정한 값이 들어감. Redis DB와 연결하는 역할을 한다.
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
//		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
//        redisStandaloneConfiguration.setHostName(host);
//        redisStandaloneConfiguration.setPort(Integer.parseInt(port));
//        redisStandaloneConfiguration.setPassword(password);
//        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
//        return lettuceConnectionFactory;
		return new LettuceConnectionFactory(host,port);
	}
	
	//RedisTemplate 사용을 위한 추가
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
    	
    	StringRedisSerializer serializer = new StringRedisSerializer();
    	// Redis와 데이터 통신을 위해 문자열을 직렬화,역직렬화하는 데 사용
    	
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(serializer);
        redisTemplate.setValueSerializer(serializer);
        return redisTemplate;
    }
}
