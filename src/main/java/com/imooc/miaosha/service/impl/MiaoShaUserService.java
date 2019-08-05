package com.imooc.miaosha.service.impl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.imooc.miaosha.dao.MiaoShaUserDao;
import com.imooc.miaosha.domain.MiaoShaUser;
import com.imooc.miaosha.excption.GlobalException;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.redis.key.MiaoShaUserKey;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.util.MD5Util;
import com.imooc.miaosha.util.UUIDUtil;
import com.imooc.miaosha.vo.LoginVo;

@Service
public class MiaoShaUserService {
	
	public static final String COOKIE_NAME_TOKEN="token";

	@Autowired
	MiaoShaUserDao miaoShaUserDao;
	
	@Autowired
	RedisService redisService;
	
	public MiaoShaUser getById(long id) {
		MiaoShaUser user=redisService.get(MiaoShaUserKey.getById, ""+id, MiaoShaUser.class);
		if(user!=null) {
			return user;
		}
		user=miaoShaUserDao.getById(id);
		if(user!=null) {
			redisService.set(MiaoShaUserKey.getById, ""+id, user);
		}
		return user;	
	}
	
	public boolean updatePassword(String token,long id,String password) {
		MiaoShaUser user=getById(id);
		if(user==null) {
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		}
		MiaoShaUser update=new MiaoShaUser();
		update.setId(id);
		update.setPassword(MD5Util.formPassToDBPass(password, user.getSalt()));
		//dao.update(update);
		redisService.delete(MiaoShaUserKey.getById, ""+id);
		user.setPassword(update.getPassword());
		redisService.set(MiaoShaUserKey.token, token, user);
		return false;
	}
	
	public boolean login(HttpServletResponse response,LoginVo loginVo) {
		if(loginVo==null) {
			throw new GlobalException(CodeMsg.SERVER_ERROR);
		}
		
		String mobile=loginVo.getMobile();
		String formPass=loginVo.getPassword();
		MiaoShaUser user=getById(Long.parseLong(mobile));
		if(user == null) {
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		}
		//验证密码
		String dbPass=user.getPassword();
		String saltDB=user.getSalt();
		
		String calcPass=MD5Util.formPassToDBPass(formPass, saltDB);
		if(!calcPass.equals(dbPass)) {
			throw new GlobalException(CodeMsg.PASSWORD_ERROR);
		}
		
		//生成用户token，写入cookie
		String token =UUIDUtil.uuid();
		addCookie(response,token,user);
		return true;
	}

	public MiaoShaUser getByToken(HttpServletResponse response,String token) {
		if(StringUtils.isEmpty(token)) {
			return null;
		}
		MiaoShaUser user = redisService.get(MiaoShaUserKey.token, token, MiaoShaUser.class);
		if(user!=null) {
			addCookie(response,token,user);
		}
		return user;
	}
	
	private void addCookie(HttpServletResponse response,String token,MiaoShaUser user) {
		redisService.set(MiaoShaUserKey.token, token, user);
		Cookie cookie=new Cookie(COOKIE_NAME_TOKEN, token);
		cookie.setMaxAge(MiaoShaUserKey.token.expireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);
	}
}
