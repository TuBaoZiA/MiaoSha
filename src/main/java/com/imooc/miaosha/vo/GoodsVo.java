package com.imooc.miaosha.vo;

import java.util.Date;

import com.imooc.miaosha.domain.Goods;

import lombok.Getter;
import lombok.Setter;


public class GoodsVo extends Goods{
	@Getter @Setter
	private Double miaoshaPrice;
	
	@Getter @Setter
	private Integer stockCount;
	
	@Getter @Setter
	private Date startDate;
	
	@Getter @Setter
	private Date endDate;
}
