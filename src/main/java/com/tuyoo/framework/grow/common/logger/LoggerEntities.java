package com.tuyoo.framework.grow.common.logger;

import lombok.Data;

import java.util.Map;

@Data
public class LoggerEntities
{
    private Long event_time;
    private String user_id;
    private String device_id;
    private String client_id;
    private String event;
    private String project_id;
    private String type;

    private Map<String,String> lib;
    private Map<String,String> properties;
}
