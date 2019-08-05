package com.imooc.miaosha.redis.key;

public class OrderKey extends BasePrefix{

	public OrderKey(String prefix) {
		super(prefix);
	}

	public static OrderKey getMiaoShaOrderKeyByUidAndGid=new OrderKey("moug"); 
}
