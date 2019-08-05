package com.imooc.miaosha.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.imooc.miaosha.domain.MiaoShaUser;
import com.imooc.miaosha.service.impl.MiaoShaUserService;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver{

	@Autowired
	MiaoShaUserService miaoShaUserService;
	
	public Object resolveArgument(MethodParameter arg0, ModelAndViewContainer arg1, NativeWebRequest arg2,
			WebDataBinderFactory arg3) throws Exception {
		return UserContext.getUser();
	}

	public boolean supportsParameter(MethodParameter arg0) {
		Class<?> clazz=arg0.getParameterType();
		return clazz==MiaoShaUser.class;
	}

}
