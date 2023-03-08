package com.tuchuan.face_recognition_acs.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
@Configuration
public class WebMVCConfig extends WebMvcConfigurationSupport {
    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 可以被跨域请求的接口，/**此时表示所有
                .allowedMethods(new String[] {"get","post","put","delete"})
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowCredentials(false)
                .exposedHeaders("")
                .maxAge(3600);
    }
}
