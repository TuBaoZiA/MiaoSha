package com.imooc.miaosha.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.miaosha.redis.RedisService;

import lombok.extern.java.Log;

@Service
@Log
public class MQSender {
	
	@Autowired
	AmqpTemplate amqpTemplate;
	
	/*public void send(Object message) {
		String msgStr=RedisService.beanToStr(message);
		log.info("send message："+msgStr);
		amqpTemplate.convertAndSend(MQConfig.QUEUE, msgStr);
	}
	
	public void sendTopic(Object message) {
		String msgStr=RedisService.beanToStr(message);
		log.info("send topic message："+msgStr);
		amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.key1",msgStr+"1");
		amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.key2",msgStr+"2");
	}
	
	public void sendFanout(Object message) {
		String msgStr=RedisService.beanToStr(message);
		log.info("send fanout message："+msgStr);
		amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",msgStr);
	}
	
	public void sendHeader(Object message) {
		String msgStr=RedisService.beanToStr(message);
		log.info("send fanout message："+msgStr);
		MessageProperties messageProperties=new MessageProperties();
		messageProperties.setHeader("header1", "value1");
		messageProperties.setHeader("header2", "value2");
		Message obj=new Message(msgStr.getBytes(), messageProperties);
		amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE,"",obj);
	}*/

	public void sendMiaoShaMessage(MiaoShaMessage message) {
		String msgStr=RedisService.beanToStr(message);
		log.info("send message："+msgStr);
		amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, msgStr);
	}
}
