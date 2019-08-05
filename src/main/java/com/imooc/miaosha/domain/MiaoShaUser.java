package com.imooc.miaosha.domain;

import java.util.Date;

import lombok.Data;

@Data
public class MiaoShaUser {

	private Long id;
	
	private String nickname;
	
	private String password;
	
	private String salt;
	
	private String head;
	
	private Date registerDate;
	
	private Date lastLoginDate;
	
	private Integer loginCount;

}
