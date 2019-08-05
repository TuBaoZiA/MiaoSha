package com.imooc.miaosha.rabbitmq;

import com.imooc.miaosha.domain.MiaoShaUser;

import lombok.Data;

@Data
public class MiaoShaMessage {
	private MiaoShaUser user;
	private long goodsId;
}
