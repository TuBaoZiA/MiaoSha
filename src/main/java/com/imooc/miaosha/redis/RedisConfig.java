package com.imooc.miaosha.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix="redis")
@Data
/**
 * 加载redis配置项
 */
public class RedisConfig {
			
	private String host;
	
	private int port;
	
	private int timeout;
	
	private String password;
	
	private int poolMaxTotal;
	
	private int poolMaxIdle;
	
	private int poolMaxWait;
	
	
}
