package com.tuyoo.framework.grow.common.logger;

import lombok.Data;

@Data
public class RequestEntities
{
    private String event;
    private String userId;
    private String userName;
    private String requestId;
    private String projectId;
    private String modelName;
}
