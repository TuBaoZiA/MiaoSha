package com.imooc.miaosha.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.alibaba.druid.util.StringUtils;
import com.imooc.miaosha.util.ValidatorUtil;

/**
 * 手机号验证具体实现
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile,String>{

	private boolean required=false;
	
	public void initialize(IsMobile paramA) {
		required=paramA.required();
	}

	public boolean isValid(String paramT, ConstraintValidatorContext paramConstraintValidatorContext) {
		if(required) {
			return ValidatorUtil.isMobile(paramT);
		}else {
			if(StringUtils.isEmpty(paramT)) {
				return true;
			}else {
				return ValidatorUtil.isMobile(paramT);
			}
		}
	}
	
}
