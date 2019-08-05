package com.imooc.miaosha.redis.key;

/**
 * rediskey前缀基类
 */
public abstract class BasePrefix implements KeyPrefix{

	private int expireSeconds;
	
	private String prefix;
	
	public BasePrefix(String prefix) {
		this(0, prefix);
	}
	
	public BasePrefix(int expireSeconds,String prefix) {
		this.expireSeconds=expireSeconds;
		this.prefix=prefix;
	}
	
	public int expireSeconds() {//0代表不过期
		return expireSeconds;
	}
	
	public String getPrefix() {
		String className=getClass().getSimpleName();
		return className+":"+prefix;
	}
	

}
