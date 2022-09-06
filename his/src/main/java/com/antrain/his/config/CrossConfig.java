package com.antrain.his.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 解决跨域：
 * 页面跨域访问Controller过滤
 */

@Configuration
public class CrossConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //允许跨域访问的路径（路由）
                .allowedOrigins("*") //允许跨域访问的源（域名）
                .allowedMethods("GET","HEAD","POST","PUT","DELETE","OPTIONS") //允许请求方法
                .allowCredentials(true) //是否允许证书cookies
                .maxAge(3600) //预测间隔时间
                .allowedHeaders("*"); //允许请求头，默认可以允许所有的请求头；
    }
}
