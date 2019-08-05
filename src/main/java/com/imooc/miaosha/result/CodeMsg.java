package com.imooc.miaosha.result;

/**
 * 消息封装
 */
public class CodeMsg {
	private int code;
	
	private String msg;
	
	private CodeMsg(int code,String msg) {
		this.code=code;
		this.msg=msg;
	}
	public static final CodeMsg SUCCEESS=new CodeMsg(0, "");
	
	
	public static final CodeMsg SERVER_ERROR=new CodeMsg(500100, "服务端异常");
	public static final CodeMsg BIND_ERROR=new CodeMsg(500101, "参数校验异常：%s");
	public static final CodeMsg SESSION_ERROR=new CodeMsg(500102, "登陆状态失效");
	public static final CodeMsg REQUEST_ILLEGAL=new CodeMsg(500103, "请求非法！");
	public static final CodeMsg ACCESS_LIMIT_REACHED=new CodeMsg(500104, "访问过于频繁！");
	
	public static final CodeMsg PASSWORD_EMPTY=new CodeMsg(500201, "登录密码不能为空");
	public static final CodeMsg PASSWORD_ERROR=new CodeMsg(500205, "密码错误");
	public static final CodeMsg MOBILE_EMPTY=new CodeMsg(500202, "手机号不能为空");
	public static final CodeMsg MOBILE_ERROR=new CodeMsg(500203, "手机号格式错误");
	public static final CodeMsg MOBILE_NOT_EXIST=new CodeMsg(500204, "手机号不存在");
	
	
	public static final CodeMsg MIAOSHA_OVER=new CodeMsg(500301, "商品数量不足，秒杀失败！");
	public static final CodeMsg MIAOSHA_REPEATE=new CodeMsg(500302, "你已秒杀过该商品！");
	public static final CodeMsg MIAOSHA_FAIL=new CodeMsg(500303, "秒杀失败！");
	
	public static final CodeMsg ORDER_NOT_EXIST=new CodeMsg(500400, "订单不存在");
	public int getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
	
	public CodeMsg fillArgs(Object... args) {
		int code=this.code;
		String message=String.format(this.msg, args);
		return new CodeMsg(code, message);
	}
	
}
