package com.imooc.miaosha.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.imooc.miaosha.domain.MiaoShaUser;

@Mapper
public interface MiaoShaUserDao {
	
	@Select("select * from miaosha_user where id = #{id}")
	public MiaoShaUser getById(@Param(value = "id") long id);
	
}
