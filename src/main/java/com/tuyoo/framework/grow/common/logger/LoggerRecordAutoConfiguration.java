package com.tuyoo.framework.grow.common.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
@EnableConfigurationProperties(LoggerProperties.class)
@ConditionalOnProperty(name = "common.logger.advice", havingValue = "true")
public class LoggerRecordAutoConfiguration implements WebMvcConfigurer
{
    public LoggerRecordAutoConfiguration()
    {
        log.info("GA-COMMON =========> LoggerRecordAutoConfiguration action............");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(myInterceptor()).addPathPatterns("/**");
    }

    @Bean
    LoggerInterceptor myInterceptor()
    {
        return new LoggerInterceptor();
    }
}
