package com.tuyoo.framework.grow.common.logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.File;

@EnableAsync
@Configuration
@EnableHystrix
@ConditionalOnWebApplication
@EnableConfigurationProperties(LoggerProperties.class)
@EnableFeignClients("com.tuyoo.framework.grow.common.feign")
public class LoggerAutoConfiguration
{
    @Autowired
    LoggerProperties loggerProperties;

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
