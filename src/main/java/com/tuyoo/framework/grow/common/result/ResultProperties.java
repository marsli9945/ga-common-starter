package com.tuyoo.framework.grow.common.result;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "common.result")
class ResultProperties
{
    private boolean enable;
    private boolean enableCors;
    private boolean enableNoPack;

    public boolean getEnableCors()
    {
        return enableCors;
    }

    public boolean getEnableNoPack()
    {
        return enableNoPack;
    }
}
