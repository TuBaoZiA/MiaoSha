package com.imooc.miaosha.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.impl.MiaoShaUserService;
import com.imooc.miaosha.vo.LoginVo;

@Controller
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	MiaoShaUserService miaoShaUserService;

	@RequestMapping("/to_login")
	public String toLogin() {
		return "login";
	}
	
	@RequestMapping("/do_login")
	@ResponseBody
	public Result<CodeMsg> doLogin(HttpServletResponse response,@Valid LoginVo loginVo) {
		miaoShaUserService.login(response,loginVo);
		return Result.success(CodeMsg.SUCCEESS);
	}
	
	
}
