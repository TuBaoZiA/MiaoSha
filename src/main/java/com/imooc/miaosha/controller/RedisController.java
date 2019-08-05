package com.imooc.miaosha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.redis.key.UserKey;
import com.imooc.miaosha.result.Result;

@Controller
@RequestMapping("/user")
public class RedisController {
	
	@Autowired
	private RedisService redisService;
	/*
	@Autowired
	private MQSender sender;*/
	
	/*@RequestMapping("/mq")
	@ResponseBody
	public Result<String> mq() {
		sender.send("hello mq!");
		return Result.success("send...");
	}  
	
	@RequestMapping("/mq/topic")
	@ResponseBody
	public Result<String> topic() {
		sender.sendTopic("hello mq Topic!");
		return Result.success("send...");
	}  
	
	@RequestMapping("/mq/fanout")
	@ResponseBody
	public Result<String> fanout() {
		sender.sendFanout("hello mq fanout!");
		return Result.success("send...");
	}  
	
	@RequestMapping("/mq/header")
	@ResponseBody
	public Result<String> header() {
		sender.sendHeader("hello mq header!");
		return Result.success("send...");
	}  */
	
	@RequestMapping("/get/{key}")
	@ResponseBody
	public Result<String> getKey(@PathVariable String key) {
		return Result.success(redisService.get(UserKey.getById,key, String.class));
	}  
	
	@RequestMapping("/set/{key}/{value}")
	@ResponseBody
	public Result<Boolean> setValue(@PathVariable String key,@PathVariable String value) {
		return Result.success(redisService.set(UserKey.getById,key, value));
	}
}
