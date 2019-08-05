package com.imooc.miaosha.redis.key;

public class MiaoShaKey extends BasePrefix{
	
	public MiaoShaKey(Integer expireSeconds,String prefix) {
		super(expireSeconds,prefix);
	}
		
	public static MiaoShaKey isGoodsOver=new MiaoShaKey(0,"go");
	public static MiaoShaKey getMiaoShaPath=new MiaoShaKey(60,"gp");
	public static MiaoShaKey getMiaoshaVerifyCode=new MiaoShaKey(300,"gvc");
}
