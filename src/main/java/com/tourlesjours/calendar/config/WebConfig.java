package com.tourlesjours.calendar.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    // 어플리케이션 외부 자원에 접근할 수 있음.
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("planUploadImg/**")
                .addResourceLocations("file:///c://calendar/upload/");
    }
}
