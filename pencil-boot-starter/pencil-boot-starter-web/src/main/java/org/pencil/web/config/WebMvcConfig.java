package org.pencil.web.config;

import org.pencil.web.interceptor.PencilMvcInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author pencil
 * @date 24/10/11 21:24
 */
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new PencilMvcInterceptor())
                .addPathPatterns("/**");
    }
}
