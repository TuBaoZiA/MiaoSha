package com.imooc.miaosha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.miaosha.domain.MiaoShaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.impl.GoodsService;
import com.imooc.miaosha.service.impl.OrderService;
import com.imooc.miaosha.vo.GoodsVo;
import com.imooc.miaosha.vo.OrderDetailVo;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	OrderService orderService;

	@Autowired
	GoodsService goodsService;
	
	@RequestMapping("/detail")
	@ResponseBody
	public Result<OrderDetailVo> orderDetail(MiaoShaUser user,@RequestParam("orderId") long orderId){
		if(user==null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		OrderInfo order=orderService.getOrderById(orderId);
		if(order==null) {
			return Result.error(CodeMsg.ORDER_NOT_EXIST);
		}
		GoodsVo goods=goodsService.getGoodsVoById(order.getGoodsId());
		return Result.success(new OrderDetailVo(goods, order));
	}
}
