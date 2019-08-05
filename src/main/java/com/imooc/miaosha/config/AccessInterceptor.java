package com.imooc.miaosha.config;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.imooc.miaosha.annotation.AccessLimit;
import com.imooc.miaosha.domain.MiaoShaUser;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.redis.key.AccessKey;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.impl.MiaoShaUserService;

@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired
	MiaoShaUserService miaoShaUserService;
	
	@Autowired
	RedisService redisService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(handler instanceof HandlerMethod) {
			MiaoShaUser user=getUser(request, response);
			UserContext.setUser(user);
			
			HandlerMethod method=(HandlerMethod) handler;
			AccessLimit accessLimit=method.getMethodAnnotation(AccessLimit.class);
			if(accessLimit == null) {
				return true;
			}
			boolean needLogin=accessLimit.needLogin();
			
			String key=request.getRequestURI();
			if(needLogin) {
				if(user==null) {
					render(response,CodeMsg.SESSION_ERROR);
					return false;
				}
				key+="_"+user.getId();
			}else {
				//do nothing
			}
			
			int seconds=accessLimit.seconds();
			int maxCount=accessLimit.maxCount();
			
			AccessKey accessKey=AccessKey.withExpire(seconds);
			Integer count=redisService.get(accessKey, ""+key, Integer.class);
			if(count==null) {
				redisService.set(accessKey, ""+key, 1);
			}else if(count < maxCount){
				redisService.incr(accessKey, ""+key);
			}else{
				render(response,CodeMsg.ACCESS_LIMIT_REACHED);
				return false;
			}
		}
		return true;
	}
	
	private void render(HttpServletResponse response,CodeMsg sessionError) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		OutputStream outputStream=response.getOutputStream();
		String str=JSON.toJSONString(Result.error(sessionError));
		outputStream.write(str.getBytes("UTF-8"));
		outputStream.flush();
		outputStream.close();
	}

	private MiaoShaUser getUser(HttpServletRequest request, HttpServletResponse response) {
		String paramToken =request.getParameter(MiaoShaUserService.COOKIE_NAME_TOKEN);
		String cookieToken=getCookieValue(request,MiaoShaUserService.COOKIE_NAME_TOKEN);
		if(StringUtils.isEmpty(cookieToken)&&StringUtils.isEmpty(paramToken)) {
			return null;
		}
		String token =StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
		return miaoShaUserService.getByToken(response,token);
	}
	
	private String getCookieValue(HttpServletRequest request, String cookieNameToken) {
		Cookie [] cookies=request.getCookies();
		if(cookies==null||cookies.length==0) {
			return null;
		}
		for (Cookie cookie : cookies) {
			if(cookie.getName().equals(cookieNameToken)) {
				return cookie.getValue();
			}
		}
		return null;
	}
}
