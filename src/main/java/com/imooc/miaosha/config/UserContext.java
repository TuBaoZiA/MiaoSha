package com.imooc.miaosha.config;


import com.imooc.miaosha.domain.MiaoShaUser;

public class UserContext {
	/**
	 * 每个线程独有
	 */
	private static ThreadLocal<MiaoShaUser> userHolder=new ThreadLocal<MiaoShaUser>();

	public static void setUser(MiaoShaUser user) {
		userHolder.set(user);
	}
	
	public static MiaoShaUser getUser() {
		return userHolder.get();
	}
	
	public static void remove() {
		userHolder.remove();
	}
}

