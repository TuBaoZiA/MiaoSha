package com.imooc.miaosha.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import com.imooc.miaosha.domain.MiaoShaUser;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.redis.key.GoodsKey;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.impl.GoodsService;
import com.imooc.miaosha.service.impl.MiaoShaUserService;
import com.imooc.miaosha.vo.GoodsDetailVo;
import com.imooc.miaosha.vo.GoodsVo;

@Controller
@RequestMapping("/goods")
public class GoodsController {
	
	@Autowired
	MiaoShaUserService miaoShaUserService;
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	ThymeleafViewResolver thymeleafViewResolver;
	
	@Autowired
	ApplicationContext applicationContext;

	@RequestMapping(value="/to_list",produces="text/html")
	@ResponseBody
	public String goodList(HttpServletRequest request,HttpServletResponse response,MiaoShaUser user,Model model) {
		model.addAttribute("user",user);
		String html=redisService.get(GoodsKey.goodsList, "", String.class);
		if(!StringUtils.isEmpty(html)) {
			return html;
		}
		
		List<GoodsVo> goodsVos=goodsService.selectGoodsVo();
		model.addAttribute("goodsList", goodsVos);
		
		SpringWebContext context=new SpringWebContext(request, response, request.getServletContext(), 
				request.getLocale(), model.asMap(), applicationContext);
		//手动渲染
		html = thymeleafViewResolver.getTemplateEngine().process("goods_list", context);
		if(!StringUtils.isEmpty(html)) {
			redisService.set(GoodsKey.goodsList, "", html);
		}
		return html;
	}
	
	@RequestMapping(value="/to_detail2/{goodsId}",produces="text/html")
	@ResponseBody
	public String goodDetail2(HttpServletRequest request,HttpServletResponse response,MiaoShaUser user,Model model,@PathVariable long goodsId) {
		model.addAttribute("user",user);
		String html=redisService.get(GoodsKey.goodsDetail, ""+goodsId, String.class);
		if(!StringUtils.isEmpty(html)) {
			return html;
		}
		 
		GoodsVo goodsVo=goodsService.getGoodsVoById(goodsId);
		
		model.addAttribute("goods", goodsVo);
		
		long startAt=goodsVo.getStartDate().getTime();
		long endAt=goodsVo.getEndDate().getTime();
		long now=System.currentTimeMillis();
		
		int miaoshaStatus=0;
		int remainSeconds=0;
		if(now<startAt) {	//秒杀未开始
			miaoshaStatus=0;
			remainSeconds=(int)(startAt-now)/1000;
		}else if(now>endAt){ //秒杀已结束
			miaoshaStatus=2;
			remainSeconds=-1;
		}else {	//秒杀进行中
			miaoshaStatus=1;
			remainSeconds=0;
		}
		model.addAttribute("miaoshaStatus", miaoshaStatus);
		model.addAttribute("remainSeconds", remainSeconds);
		
		
		SpringWebContext context=new SpringWebContext(request, response, request.getServletContext(), 
				request.getLocale(), model.asMap(), applicationContext);
		//手动渲染
		html=thymeleafViewResolver.getTemplateEngine().process("goods_detail", context);
		if(!StringUtils.isEmpty(html)) {
			redisService.set(GoodsKey.goodsDetail, ""+goodsId, html);
		}
		return html;
	}
	
	@RequestMapping(value="/detail/{goodsId}")
	@ResponseBody
	public Result<GoodsDetailVo> goodDetail(HttpServletRequest request,HttpServletResponse response,MiaoShaUser user,Model model,@PathVariable long goodsId) {
		GoodsVo goodsVo=goodsService.getGoodsVoById(goodsId);
		
		long startAt=goodsVo.getStartDate().getTime();
		long endAt=goodsVo.getEndDate().getTime();
		long now=System.currentTimeMillis();
		
		int miaoshaStatus=0;
		int remainSeconds=0;
		if(now<startAt) {	//秒杀未开始
			miaoshaStatus=0;
			remainSeconds=(int)(startAt-now)/1000;
		}else if(now>endAt){ //秒杀已结束
			miaoshaStatus=2;
			remainSeconds=-1;
		}else {	//秒杀进行中
			miaoshaStatus=1;
			remainSeconds=0;
		}
		
		return Result.success(new GoodsDetailVo(goodsVo,user,miaoshaStatus, remainSeconds));
	}
	
	
}
