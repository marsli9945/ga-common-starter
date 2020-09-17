package com.tuyoo.framework.grow.common.logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(LoggerProperties.class)
public class LoggerAutoConfiguration
{
    @Autowired
    LoggerProperties loggerProperties;

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate()
    {
        return new RestTemplate();
    }

    @Bean
    public LoggerService loggerService()
    {
        LoggerService loggerService = new LoggerService();
        loggerService.setLoggerProperties(loggerProperties);
        LoggerLocal loggerLocal = new LoggerLocal(new File(loggerProperties.getLocalPath()));
        loggerService.setLoggerLocal(loggerLocal);
        return loggerService;
    }

}
