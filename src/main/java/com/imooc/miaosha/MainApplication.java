package com.imooc.miaosha;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication

/**
 * Springboot启动类
 * @author Yu
 *
 */
public class MainApplication /*extends SpringBootServletInitializer*/{
    
	public static void main(String[] args) {
    	SpringApplicationBuilder builder=new SpringApplicationBuilder(MainApplication.class);
        //修改Banner的模式为OFF
    	builder.bannerMode(Banner.Mode.OFF).run(args);
    } 
    
   /* @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    	return builder.sources(MainApplication.class);
    }*/
}
