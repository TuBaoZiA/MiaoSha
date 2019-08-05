package com.imooc.miaosha.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.miaosha.domain.MiaoShaOrder;
import com.imooc.miaosha.domain.MiaoShaUser;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.service.impl.GoodsService;
import com.imooc.miaosha.service.impl.MiaoShaService;
import com.imooc.miaosha.service.impl.OrderService;
import com.imooc.miaosha.vo.GoodsVo;

import lombok.extern.java.Log;


@Service
@Log
public class MQReceiver {
	
	/*@RabbitListener(queues=MQConfig.QUEUE)
	public void receive(String msg) {
		log.info("receive message："+msg);
	}
	
	@RabbitListener(queues=MQConfig.TOPIC_QUEUE1)
	public void receiveTopic1(String msg) {
		log.info("topic1 queue message："+msg);
	}
	
	@RabbitListener(queues=MQConfig.TOPIC_QUEUE2)
	public void receiveTopic2(String msg) {
		log.info("topic2 queue message："+msg);
	}
	
	@RabbitListener(queues=MQConfig.HEADER_QUEUE)
	public void receiveHeader(byte[] msg) {
		log.info("header queue message："+new String(msg));
	}*/
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	MiaoShaService miaoShaService;
	
	@Autowired
	RedisService redisService;
	
	@RabbitListener(queues=MQConfig.MIAOSHA_QUEUE)
	public void receive(String msg) {
		log.info("receive message："+msg);
		MiaoShaMessage message=RedisService.strToBean(msg, MiaoShaMessage.class);
		MiaoShaUser user=message.getUser();
		long goodsId=message.getGoodsId();
		
		GoodsVo goodsVo=goodsService.getGoodsVoById(goodsId);
		int stock = goodsVo.getStockCount();
		if(stock<=0) {
			return;
		}
		
		//判断当前用户是否秒杀过
		MiaoShaOrder order=orderService.getMiaoShaOrderByUserIdAndGoodsId(user.getId(),goodsId);
		if(order!=null) {
			return;
		}
		
		//减库存，下订单
		miaoShaService.miaosha(user,goodsVo);
	}
}
