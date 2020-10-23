package com.tuyoo.framework.grow.common.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Slf4j
@Configuration
@EnableConfigurationProperties(LoggerProperties.class)
public class LoggerAutoConfiguration
{
    @Autowired
    LoggerProperties loggerProperties;

    @Bean(name = "microRestTemplate")
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

        File file = new File(loggerProperties.getLocalPath());
        if(!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                log.info("GA-COMMON =========> 日志临时文件目录创建失败");
            }
        }
        try
        {
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    log.info("GA-COMMON =========> 日志临时文件创建失败");
                }
            }
        } catch (Exception e) {
            log.info("GA-COMMON =========> 日志临时文件创建失败");
        }

        LoggerLocal loggerLocal = new LoggerLocal(file);
        loggerService.setLoggerLocal(loggerLocal);
        return loggerService;
    }

}
