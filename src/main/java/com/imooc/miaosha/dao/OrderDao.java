package com.imooc.miaosha.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import com.imooc.miaosha.domain.MiaoShaOrder;
import com.imooc.miaosha.domain.OrderInfo;

@Mapper
public interface OrderDao {

	@Select("select * from miaosha_order where user_id=#{uId} and goods_id=#{goodsId}")
	public MiaoShaOrder getMiaoShaOrderByUserIdAndGoodsId(@Param("uId") Long id,@Param("goodsId") Long goodsId);

	@Insert("insert into order_info(user_id,goods_id,goods_name,goods_count,goods_price,order_channel,status,create_date)"
			+ "values(#{userId},#{goodsId},#{goodsName},#{goodsCount},#{goodsPrice},#{orderChannel},#{status},#{createDate})")
	@SelectKey(keyColumn="id",keyProperty="id",resultType=long.class,before=false,statement="select last_insert_id()")
	public long insert(OrderInfo orderInfo);

	@Insert("insert into miaosha_order(user_id,goods_id,order_id) values(#{userId},#{goodsId},#{orderId})")
	public int insertMiaoShaOrder(MiaoShaOrder miaoShaOrder);

	@Select("select * from order_info where id=#{orderId}")
	public OrderInfo getOrderById(@Param("orderId")long orderId);

}
