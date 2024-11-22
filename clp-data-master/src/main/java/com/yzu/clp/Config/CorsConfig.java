package com.yzu.clp.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig  implements WebMvcConfigurer  {

    @Override
    public void addCorsMappings(CorsRegistry registry){

        registry.addMapping("/**")
                .allowCredentials(true)//是否发送Cookie
                .allowedOriginPatterns("*")//设置放行哪些原始域
                .allowedMethods("*")//放行哪些请求方式
                .allowedHeaders("*")//放行哪些原始请求头部信息
                .exposedHeaders("*");//暴露哪些原始请求头部信息
    }
}
