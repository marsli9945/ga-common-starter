package com.tuyoo.framework.grow.common.exception;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "common.exception")
public class ExceptionProperties
{
    private boolean enable;
}
