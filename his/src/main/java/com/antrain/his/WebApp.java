package com.antrain.his;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.Banner;

@MapperScan("com.antrain.his.mapper")//注释后warning取消
@SpringBootApplication
public class WebApp{

	public static void main(String[] args) {
		//直接启动不管图标
		SpringApplication.run(WebApp.class);

		//设置关闭图标
		//SpringApplication application = new SpringApplication(WebApp.class);
        //application.setBannerMode(Banner.Mode.OFF);//关闭banner图标
        //application.run(args);
	}

}
