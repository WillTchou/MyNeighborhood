package com.example.project.MyNeighborhood.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {
    @Bean
    public WebMvcConfigurer corsConfigure() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000","http://localhost:8000",
                                "https://my-neighborhood-app.com")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .allowedMethods(
                                "GET",
                                "PUT",
                                "POST",
                                "DELETE",
                                "OPTIONS"
                        );
            }
        };
    }
}
