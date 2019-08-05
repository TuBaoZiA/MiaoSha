package com.imooc.miaosha.excption;

import com.imooc.miaosha.result.CodeMsg;

import lombok.Getter;

/**
 *	自定义异常
 */
public class GlobalException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	@Getter
	private CodeMsg codeMsg;
	
	public GlobalException(CodeMsg msg) {
		super(msg.toString());
		this.codeMsg=msg;
	}

}
