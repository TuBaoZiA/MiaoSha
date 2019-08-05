package com.imooc.miaosha.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy={IsMobileValidator.class})
/**
 * 验证手机号注解
 */
public @interface IsMobile {
	
	boolean required() default true;
	
	String message() default "手机号码格式错误";
	  
	Class<?>[] groups() default {};
	  
	Class<? extends Payload>[] payload() default {};
}
