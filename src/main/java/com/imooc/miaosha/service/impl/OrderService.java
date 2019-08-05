package com.imooc.miaosha.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.miaosha.dao.OrderDao;
import com.imooc.miaosha.domain.MiaoShaOrder;
import com.imooc.miaosha.domain.MiaoShaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.redis.key.OrderKey;
import com.imooc.miaosha.vo.GoodsVo;

@Service
public class OrderService {

	@Autowired
	OrderDao orderDao;

	@Autowired
	RedisService redisService;
	
	public MiaoShaOrder getMiaoShaOrderByUserIdAndGoodsId(Long id, Long goodsId) {
		return redisService.get(OrderKey.getMiaoShaOrderKeyByUidAndGid, ""+id+"_"+goodsId, MiaoShaOrder.class);
	}

	@Transactional
	public OrderInfo createOrder(MiaoShaUser user, GoodsVo goodsVo) {
		OrderInfo orderInfo=new OrderInfo();
		orderInfo.setCreateDate(new Date());
		orderInfo.setDeliveryAddrId(0L);
		orderInfo.setGoodsCount(1);
		orderInfo.setGoodsId(goodsVo.getId());
		orderInfo.setGoodsName(goodsVo.getGoodsName());
		orderInfo.setGoodsPrice(goodsVo.getMiaoshaPrice());
		orderInfo.setOrderChannel(1);
		orderInfo.setStatus(0);
		orderInfo.setUserId(user.getId());
		orderDao.insert(orderInfo);
		
		MiaoShaOrder miaoShaOrder=new MiaoShaOrder();
		miaoShaOrder.setGoodsId(goodsVo.getId());
		miaoShaOrder.setOrderId(orderInfo.getId());
		miaoShaOrder.setUserId(user.getId());
		orderDao.insertMiaoShaOrder(miaoShaOrder);
		
		redisService.set(OrderKey.getMiaoShaOrderKeyByUidAndGid, ""+user.getId()+"_"+goodsVo.getId(), miaoShaOrder);
		return orderInfo;
	}

	public OrderInfo getOrderById(long orderId) {
		return orderDao.getOrderById(orderId);
	}
}
