package com.nowcoder.community.config;

import com.nowcoder.community.controller.interceptor.AlphaInterceptor;
import com.nowcoder.community.controller.interceptor.LoginRequiredInterceptor;
import com.nowcoder.community.controller.interceptor.LoginTicketInterceptor;
import com.nowcoder.community.controller.interceptor.MessageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.invoke.MethodHandle;

/**
 * ClassName: WebMvcConfig
 * Package: com.nowcoder.community.config
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/5/28 18:49
 * @Version 1.0
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    //@Autowired
    //private AlphaInterceptor alphaInterceptor;

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    // 废弃拦截器，改用 Spring Security 做权限控制
    //@Autowired
    //private LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    private MessageInterceptor messageInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(alphaInterceptor)
        //        .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg")
        //        .addPathPatterns("/register", "/login");

        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

        //registry.addInterceptor(loginRequiredInterceptor)
        //        .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

        registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");
    }
}
