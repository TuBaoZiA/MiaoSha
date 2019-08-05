package com.imooc.miaosha.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.imooc.miaosha.redis.key.KeyPrefix;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
/**
 * 封装redis
 */
public class RedisService {
	
	@Autowired
	JedisPool jedisPool;
	
	/**
	 * 获取key值
	 * @param prefix
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> T get(KeyPrefix prefix,String key,Class<T> clazz) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			//生成真正的key
			String realKey=prefix.getPrefix()+key;
			String str=jedis.get(realKey);
			return strToBean(str,clazz);
		} finally {
			returnToPool(jedis);
		}
	}
	
	/**
	 * 设置值
	 * @param prefix
	 * @param key
	 * @param value
	 * @return
	 */
	public <T> boolean set(KeyPrefix prefix,String key,T value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String str=beanToStr(value);
			if(str==null||str.length() <= 0) {
				return false;
			}
			//生成真正的key
			String realKey=prefix.getPrefix()+key;
			int seconds=prefix.expireSeconds();
			if(seconds<=0) {
				jedis.set(realKey, str);
			}else {
				jedis.setex(realKey,seconds,str);
			}
			return true;
		} finally {
			returnToPool(jedis);
		}
	}
	
	/**
	 * 判断key是否存在
	 * @param prefix
	 * @param key
	 * @return
	 */
	public <T> boolean exists(KeyPrefix prefix,String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			//生成真正的key
			String realKey=prefix.getPrefix()+key;
			return jedis.exists(realKey);
		} finally {
			returnToPool(jedis);
		}
	}
	
	/**
	 * 值++ (原子操作)
	 * @param prefix
	 * @param key
	 * @return
	 */
	public <T> Long incr(KeyPrefix prefix,String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			//生成真正的key
			String realKey=prefix.getPrefix()+key;
			return jedis.incr(realKey);
		} finally {
			returnToPool(jedis);
		}
	}
	
	/**
	 * 值--
	 * @param prefix
	 * @param key
	 * @return
	 */
	public <T> Long decr(KeyPrefix prefix,String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			//生成真正的key
			String realKey=prefix.getPrefix()+key;
			return jedis.decr(realKey);
		} finally {
			returnToPool(jedis);
		}
	}
	
	/**
	 * 删除
	 * @param prefix
	 * @param key
	 * @return
	 */
	public boolean delete(KeyPrefix prefix,String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			//生成真正的key
			String realKey=prefix.getPrefix()+key;
			return jedis.del(realKey)>0;
		} finally {
			returnToPool(jedis);
		}
	}
	
	public static <T> String beanToStr(T value) {
		if(value==null) {
			return null;
		}
		Class<?> clazz=value.getClass();
		if(clazz==int.class||clazz==Integer.class) {
			return ""+value;
		}else if(clazz==String.class){
			return (String)value;
		}else if(clazz==long.class||clazz==Long.class) {
			return ""+value;
		}else {
			return JSON.toJSONString(value);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T strToBean(String str,Class<T> clazz) {
		if(str==null||str.length()<=0||clazz==null) {
			return null;
		}
		if(clazz==int.class||clazz==Integer.class) {
			return (T) Integer.valueOf(str);
		}else if(clazz==String.class){
			return (T) str;
		}else if(clazz==long.class||clazz==Long.class) {
			return (T) Long.valueOf(str);
		}else {
			return JSON.toJavaObject(JSON.parseObject(str), clazz);
		}
	}
	private void returnToPool(Jedis jedis) {
		if(jedis!=null) {
			jedis.close();
		}
	}
 	
}
