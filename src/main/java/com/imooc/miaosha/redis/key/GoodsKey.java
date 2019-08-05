package com.imooc.miaosha.redis.key;

public class GoodsKey extends BasePrefix{

	public GoodsKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	
	public static GoodsKey goodsList=new GoodsKey(60, "gl");
	public static GoodsKey goodsDetail=new GoodsKey(60, "gd");
	public static GoodsKey miaoShaGoodsStock=new GoodsKey(0, "gs");
}
