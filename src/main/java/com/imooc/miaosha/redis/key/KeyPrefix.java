package com.imooc.miaosha.redis.key;

public interface KeyPrefix {

	public int expireSeconds();

	public String getPrefix();
}
