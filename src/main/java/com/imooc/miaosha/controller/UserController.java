package com.imooc.miaosha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.miaosha.domain.MiaoShaUser;
import com.imooc.miaosha.result.Result;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@RequestMapping("/info")
	@ResponseBody
	public Result<MiaoShaUser> getUserInfo(MiaoShaUser user) {
		return Result.success(user);
	}
}
