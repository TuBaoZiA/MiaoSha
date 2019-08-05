package com.imooc.miaosha.result;

/**
 * 返回前端信息封装
 * @param <T>
 */
public class Result<T> {

	private int code;
	
	private String msg;

	private T data;

	
	private Result(T data) {
		this(0,"",data);
	}
	
	private Result(int code,String msg){
		this(code, msg, null);
	}
	
	private Result(int code,String msg,T data) {
		this.code=code;
		this.msg=msg;
		this.data=data;
	}
	
	public static <T> Result<T> success(T data){
		return new Result<T>(data);
	}
	
	public static <T> Result<T> error(CodeMsg msg){
		return new Result<T>(msg.getCode(), msg.getMsg());
	}
	
	public int getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
	public T getData() {
		return data;
	}
}
