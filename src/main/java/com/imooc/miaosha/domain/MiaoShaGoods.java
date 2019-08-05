package com.imooc.miaosha.domain;

import java.util.Date;

import lombok.Data;

@Data
public class MiaoShaGoods {
	
	private Long id;
	
	private Long goodsId;
	
	private Integer stockCount;
	
	private Date startDate;
	
	private Date endDate;
	
	public MiaoShaGoods() {}
	
	public MiaoShaGoods(Long goodsId) {
		this.goodsId=goodsId;
	}
}
