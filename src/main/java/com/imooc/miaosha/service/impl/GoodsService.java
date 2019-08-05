package com.imooc.miaosha.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.miaosha.dao.GoodsDao;
import com.imooc.miaosha.domain.MiaoShaGoods;
import com.imooc.miaosha.vo.GoodsVo;

@Service
public class GoodsService {

	@Autowired
	GoodsDao goodsDao;
	
	public List<GoodsVo> selectGoodsVo(){
		return goodsDao.selectGoodsVo();
	}
	
	public GoodsVo getGoodsVoById(long id) {
		return goodsDao.getGoodsVoById(id);
	}

	public boolean reduceStock(GoodsVo goodsVo) { 
		int ret=goodsDao.reduceStock(new MiaoShaGoods(goodsVo.getId()));
		return ret>0;
	}
}
