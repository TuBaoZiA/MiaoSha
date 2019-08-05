package com.imooc.miaosha.util;

import java.util.UUID;

/**
 * UUID工具类
 *
 */
public class UUIDUtil {
	
	/**
	 * 生成UUID并去除‘-’
	 * @return
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}

}
