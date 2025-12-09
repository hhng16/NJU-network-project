package com.th.marketgod.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射本地文件路径到虚拟路径
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///var/upload/upimage/");
        registry.addResourceHandler("/heads/**")
                .addResourceLocations("file:///var/upload/uphead/");

        logger.info("配置静态资源映射: /heads/** -> file:///var/upload/uphead/");

        logger.info("配置静态资源映射: /images/** -> file:///var/upload/upimage/");
    }
}



