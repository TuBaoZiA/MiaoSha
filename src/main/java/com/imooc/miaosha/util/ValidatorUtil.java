package com.imooc.miaosha.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.druid.util.StringUtils;

/**
 * 校验工具类
 *
 */
public class ValidatorUtil {
	
	private static final Pattern MOBILE_PATTERN=Pattern.compile("1\\d{10}");
	
	public static boolean isMobile(String src) {
		if(StringUtils.isEmpty(src)) {
			return false;
		}
		Matcher matcher=MOBILE_PATTERN.matcher(src);
		return matcher.matches();
	}
}
