package org.example.handle_excel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@SuppressWarnings("all")

/**
 * 配置类，用于设置跨域资源共享（CORS）策略。
 * 允许来自特定来源的请求，配置允许的 HTTP 方法和请求头。
 */
@Configuration
public class CorsConfig {
    /**
     * 定义一个 WebMvcConfigurer Bean，用于配置 CORS 规则。
     *
     * @return WebMvcConfigurer 实例，配置了 CORS 规则
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            /**
             * 配置 CORS 映射规则。
             *
             * @param registry CorsRegistry 对象，用于添加 CORS 配置
             */
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 允许所有路径
                        .allowedOrigins("http://localhost:5174") // 允许的来源
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的 HTTP 方法
                        .allowedHeaders("*") // 允许的请求头
                        .allowCredentials(true); // 是否允许发送凭证（如 Cookies）
            }
        };
    }
}
