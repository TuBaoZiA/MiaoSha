package com.imooc.miaosha.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.miaosha.annotation.AccessLimit;
import com.imooc.miaosha.domain.MiaoShaOrder;
import com.imooc.miaosha.domain.MiaoShaUser;
import com.imooc.miaosha.rabbitmq.MQSender;
import com.imooc.miaosha.rabbitmq.MiaoShaMessage;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.redis.key.GoodsKey;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.impl.GoodsService;
import com.imooc.miaosha.service.impl.MiaoShaService;
import com.imooc.miaosha.service.impl.OrderService;
import com.imooc.miaosha.vo.GoodsVo;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean{
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	MiaoShaService miaoShaService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	MQSender sender;

	private Map<Long, Boolean> localOverMap=new HashMap<Long, Boolean>();
	
	/**
	 * 系统初始化时调用
	 */
	public void afterPropertiesSet() throws Exception {
		List<GoodsVo> goodsVos=goodsService.selectGoodsVo();
		if(goodsVos==null) {
			return;
		}
		
		for(GoodsVo goodsVo:goodsVos) {
			redisService.set(GoodsKey.miaoShaGoodsStock, ""+goodsVo.getId(), goodsVo.getStockCount());
			localOverMap.put(goodsVo.getId(), false);
		}
	}
	
	/**
	 * GET 幂等(获取数据)
	 * POST 提交数据
	 */
	@RequestMapping(value="/{path}/do_miaosha",method=RequestMethod.POST)
	@ResponseBody
	public Result<Integer> miaosha(Model model,MiaoShaUser user,@RequestParam Long goodsId,@PathVariable("path") String path) {
		model.addAttribute("user", user);
		if(user==null) {
			return Result.error(CodeMsg.SERVER_ERROR);
		}
		boolean check=miaoShaService.checkPath(user.getId(),goodsId,path);
		if(!check) {
			return Result.error(CodeMsg.REQUEST_ILLEGAL);
		}
		
		//内存标记，减少对redis的访问
		boolean over=localOverMap.get(goodsId);
		if(over) {
			return Result.error(CodeMsg.MIAOSHA_OVER);
		}
		
		//预减库存
		long stock=redisService.decr(GoodsKey.miaoShaGoodsStock, ""+goodsId);
		if(stock<0) {
			localOverMap.put(goodsId, true);
			return Result.error(CodeMsg.MIAOSHA_OVER);
		}
		
		MiaoShaOrder order=orderService.getMiaoShaOrderByUserIdAndGoodsId(user.getId(),goodsId);
		if(order!=null) {
			return Result.error(CodeMsg.MIAOSHA_REPEATE);
		}
		
		//入队
		MiaoShaMessage message=new MiaoShaMessage();
		message.setUser(user);
		message.setGoodsId(goodsId);
		sender.sendMiaoShaMessage(message);
		return Result.success(0);
	}
	
	/**
	 * orderId:成功
	 * -1：秒杀失败
	 * 0：排队中
	 * @param model
	 * @param user
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(value="/result")
	@ResponseBody
	public Result<Long> miaoshaResult(Model model,MiaoShaUser user,@RequestParam Long goodsId) {
		model.addAttribute("user",user);
		if(user==null) {
			return Result.error(CodeMsg.SERVER_ERROR);
		}
		long result=miaoShaService.getMiaoShaResult(user.getId(),goodsId);
		return Result.success(result);
	}
	
	
	@AccessLimit(seconds=5,maxCount=5,needLogin=true)
	@RequestMapping(value="/path")
	@ResponseBody
	public Result<String> miaoshaPath(HttpServletRequest request,Model model,MiaoShaUser user,@RequestParam Long goodsId,
			@RequestParam(value="verifyCode",defaultValue="0") int verifyCode) {
		model.addAttribute("user",user);
		if(user==null) {
			return Result.error(CodeMsg.SERVER_ERROR);
		}
		
		//验证码校验
		boolean check=miaoShaService.checkVerifyCode(user.getId(), goodsId, verifyCode);
		if(!check) {
			return Result.error(CodeMsg.REQUEST_ILLEGAL);
		}
		
		String path=miaoShaService.createMiaoShaPath(user.getId(),goodsId);
		
		return Result.success(path);
	}

	@RequestMapping(value="/verifyCode")
	@ResponseBody
	public Result<String> verifyCode(HttpServletResponse res,Model model,MiaoShaUser user,@RequestParam Long goodsId) {
		model.addAttribute("user",user);
		if(user==null) {
			return Result.error(CodeMsg.SERVER_ERROR);
		}
		BufferedImage image=miaoShaService.createVerifyCode(user.getId(),goodsId);
		
		try {
			OutputStream outputStream=res.getOutputStream();
			ImageIO.write(image, "JPEG", outputStream);
			outputStream.flush();
			outputStream.close();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return Result.error(CodeMsg.MIAOSHA_FAIL);
		} 
	}
	
}
