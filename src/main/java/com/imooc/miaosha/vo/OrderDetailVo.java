package com.imooc.miaosha.vo;

import com.imooc.miaosha.domain.OrderInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailVo {
	private GoodsVo goods;
	
	private OrderInfo order;
}
