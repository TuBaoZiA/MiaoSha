package com.imooc.miaosha.redis.key;

/**
 * redis  用户Key前缀
 *
 */
public class MiaoShaUserKey extends BasePrefix{
	
	private static final int TOKE＿EXPIRE= 3600 * 24 * 2;

	public MiaoShaUserKey(int expireSeconds,String prefix) {
		super(expireSeconds,prefix);
	}
	
	public static MiaoShaUserKey token=new MiaoShaUserKey(TOKE＿EXPIRE,"tk");
	public static MiaoShaUserKey getById=new MiaoShaUserKey(0,"id");
}
