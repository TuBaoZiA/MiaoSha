package com.imooc.miaosha.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * MD5工具类
 *
 */
public class MD5Util {
	/**固定salt 第一次MD5用*/
	private static final String SALT="root";

	public static String md5(String src) {
		return DigestUtils.md5Hex(src);
	}
	
	public static String inputPassToFormPass(String inputPass) {
		String str="" + SALT.charAt(0) + inputPass + SALT.charAt(3);
		return md5(str);
	}
	
	public static String formPassToDBPass(String formPass,String salt) {
		String str="" + salt.charAt(0) + formPass + salt.charAt(3);
		return md5(str);
	}
	
	public static String inputPassToDBPass(String input,String saltDB) {
		String fromPass=inputPassToFormPass(input);
		return formPassToDBPass(fromPass,saltDB);
	}
	
	public static void main(String[] args) {
		System.out.println(inputPassToFormPass("admin123"));
		//System.out.println(inputPassToDBPass("admin123", "root"));
	}
}
