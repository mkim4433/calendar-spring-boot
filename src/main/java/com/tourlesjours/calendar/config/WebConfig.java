package com.tourlesjours.calendar.config;

import com.tourlesjours.calendar.member.MemberSigninInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    MemberSigninInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(interceptor)
                .addPathPatterns("/member/modify")
                .excludePathPatterns(
                        "/member/signup",
                        "/member/signin",
                        "/member/signout_confirm",
                        "/member/findpassword"
                );
    }
}
