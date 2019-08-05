package com.imooc.miaosha.service.impl;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.miaosha.domain.MiaoShaOrder;
import com.imooc.miaosha.domain.MiaoShaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.redis.key.MiaoShaKey;
import com.imooc.miaosha.util.MD5Util;
import com.imooc.miaosha.util.UUIDUtil;
import com.imooc.miaosha.vo.GoodsVo;

@Service
public class MiaoShaService {

	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;

	@Autowired
	RedisService redisService;
	
	@Transactional
	public OrderInfo miaosha(MiaoShaUser user, GoodsVo goodsVo) {
		boolean success=goodsService.reduceStock(goodsVo);
		if(success) {
			return orderService.createOrder(user,goodsVo);
		}else {
			setGoodsOver(goodsVo.getId());
			return null;
		}
	}

	public long getMiaoShaResult(Long userId, Long goodsId) {
		MiaoShaOrder orderInfo=orderService.getMiaoShaOrderByUserIdAndGoodsId(userId, goodsId);
		if(orderInfo != null) {
			return orderInfo.getOrderId();
		}else {
			boolean isOver=getGoodsOver(goodsId);
			if(isOver) {
				return -1;
			}else {
				return 0;
			}
		}
	}

	private boolean getGoodsOver(Long goodsId) {
		return redisService.exists(MiaoShaKey.isGoodsOver, ""+goodsId);
	}

	private void setGoodsOver(Long goodsId) {
		redisService.set(MiaoShaKey.isGoodsOver, ""+goodsId, true);
	}

	public String createMiaoShaPath(Long userId, Long goodsId) {
		String str=MD5Util.md5(UUIDUtil.uuid()+"123456");
		redisService.set(MiaoShaKey.getMiaoShaPath, ""+userId+"_"+goodsId, str);
		return str;
	}

	public boolean checkPath(Long userId, Long goodsId, String path) {
		String string=redisService.get(MiaoShaKey.getMiaoShaPath, ""+userId+"_"+goodsId, String.class);
		return string.equals(path);
	}

	public BufferedImage createVerifyCode(Long userId, Long goodsId) {
		if(userId==null||goodsId<=0) {
			return null;
		}
		int width = 80;
		int height = 32;
		//create the image
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		// set the background color
		g.setColor(new Color(0xDCDCDC));
		g.fillRect(0, 0, width, height);
		// draw the border
		g.setColor(Color.black);
		g.drawRect(0, 0, width - 1, height - 1);
		// create a random instance to generate the codes
		Random rdm = new Random();
		// make some confusion
		for (int i = 0; i < 50; i++) {
			int x = rdm.nextInt(width);
			int y = rdm.nextInt(height);
			g.drawOval(x, y, 0, 0);
		}
		// generate a random code
		String verifyCode = generateVerifyCode(rdm);
		g.setColor(new Color(0, 100, 0));
		g.setFont(new Font("Candara", Font.BOLD, 24));
		g.drawString(verifyCode, 8, 24);
		g.dispose();
		//把验证码存到redis中
		int rnd = calc(verifyCode);
		redisService.set(MiaoShaKey.getMiaoshaVerifyCode, userId+","+goodsId, rnd);
		//输出图片	
		return image;
	}
	
	public boolean checkVerifyCode(Long userId, long goodsId, int verifyCode) {
		if(userId == null || goodsId <=0) {
			return false;
		}
		Integer codeOld = redisService.get(MiaoShaKey.getMiaoshaVerifyCode, userId+","+goodsId, Integer.class);
		if(codeOld == null || codeOld - verifyCode != 0 ) {
			return false;
		}
		redisService.delete(MiaoShaKey.getMiaoshaVerifyCode, userId+","+goodsId);
		return true;
	}
	
	private static int calc(String exp) {
		try {
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("JavaScript");
			return (Integer)engine.eval(exp);
		}catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	private static char[] ops = new char[] {'+', '-', '*'};
	/**
	 * + - * 
	 * */
	private String generateVerifyCode(Random rdm) {
		int num1 = rdm.nextInt(10);
	    int num2 = rdm.nextInt(10);
		int num3 = rdm.nextInt(10);
		char op1 = ops[rdm.nextInt(3)];
		char op2 = ops[rdm.nextInt(3)];
		String exp = ""+ num1 + op1 + num2 + op2 + num3;
		return exp;
	}
}
