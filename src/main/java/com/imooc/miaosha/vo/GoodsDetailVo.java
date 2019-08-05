package com.imooc.miaosha.vo;

import com.imooc.miaosha.domain.MiaoShaUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsDetailVo {
	private GoodsVo goods;
	
	private MiaoShaUser user;
	
	private int miaoshaStatus;
	
	private int remainSeconds;
}
