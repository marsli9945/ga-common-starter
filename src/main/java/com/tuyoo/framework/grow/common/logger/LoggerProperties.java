package com.tuyoo.framework.grow.common.logger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "common.logger")
public class LoggerProperties
{
    private String clientId = "H5_5.0_tuyoo.tuyoo.0-hall20435.tuyoo.GA";
    private String projectId = "20435";

    private boolean advice;
    private String localPath = "tmp/galog";

    private Boolean enableSend = true;
    private Boolean enableTerminal = false;
    private String sendWaitTime = "10";
    private int sendWaitSize = 10;

    private LoggerProperties.LibConfig lib = new LibConfig();
    private PropertiesConfig properties = new PropertiesConfig();

    @Data
    public static class PropertiesConfig
    {
        private String modelVersion;
        private String serviceName;
    }

    @Data
    public static class LibConfig
    {
        private String language = "java";
        private String serviceVersion;
    }
}
