package com.tuyoo.framework.grow.common.logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.tuyoo.framework.grow.common.entities.CommonResult;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

@Data
@Slf4j
public class LoggerService
{
    LoggerLocal loggerLocal;

    LoggerProperties loggerProperties;

    @Autowired
    HttpServletRequest request;

    @Autowired
    RestTemplate microRestTemplate;

    private LinkedBlockingQueue<LoggerEntities> logList = new LinkedBlockingQueue<>();

    public static final String TYPE_TRACK = "track";

    public void track(String event)
    {
        HashMap<String, String> properties = new HashMap<>();
        HashMap<String, String> lib = new HashMap<>();
        LoggerEntities loggerEntities = initEntities(properties, lib);
        loggerEntities.setEvent(event);
        record(loggerEntities);
    }

    public void track(String event, HashMap<String, String> properties, HashMap<String, String> lib)
    {
        LoggerEntities loggerEntities = initEntities(properties, lib);
        loggerEntities.setEvent(event);
        record(loggerEntities);
    }

    public void trackSelfRequest(RequestEntities requestEntities)
    {
        HashMap<String, String> properties = new HashMap<>();
        HashMap<String, String> lib = new HashMap<>();
        LoggerEntities loggerEntities = initEntitiesSelfRequest(requestEntities, properties, lib);
        record(loggerEntities);
    }

    public void trackSelfRequest(RequestEntities requestEntities, HashMap<String, String> properties, HashMap<String, String> lib)
    {
        LoggerEntities loggerEntities = initEntitiesSelfRequest(requestEntities, properties, lib);
        record(loggerEntities);
    }

    private void record(LoggerEntities loggerEntities)
    {
        if (loggerProperties.getEnableTerminal())
        {
            log.info(String.format("[%s]-[%s]", loggerProperties.getEnableSend() ? "online" : "down", JSON.toJSONString(loggerEntities)));
        }

        if (loggerProperties.getEnableSend())
        {
            try
            {
                logList.put(loggerEntities);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            if (logList.size() >= loggerProperties.getSendWaitSize())
            {
                sendList();
            }
        }
    }

    @PreDestroy
    @Scheduled(cron = "0 */${common.logger.sendWaitTime} * * * *")
    public void sendList()
    {
        ArrayList<LoggerEntities> list = new ArrayList<>(logList);
        List<LoggerEntities> local = loggerLocal.read();
        logList.clear();
        list.addAll(local);

        if (list.size() < 1)
        {
            return;
        }

        try
        {
            CommonResult commonResult = send(list);
            if (commonResult.getCode() == 200)
            {
                loggerLocal.flash();
            }
            else
            {
                loggerLocal.record(list);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            loggerLocal.record(list);
        }

    }

    private CommonResult send(ArrayList<LoggerEntities> list)
    {
        String params = JSONArray.toJSONString(list);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);

        HttpEntity<String> request = new HttpEntity<>(params, headers);
        return microRestTemplate.postForObject(loggerProperties.getUrl(), request, CommonResult.class);
    }

    private LoggerEntities initEntities(HashMap<String, String> properties, HashMap<String, String> lib)
    {
        long eventTime = System.currentTimeMillis();

        LoggerEntities loggerEntities = new LoggerEntities();
        loggerEntities.setClient_id(loggerProperties.getClientId());
        loggerEntities.setType(TYPE_TRACK);
        loggerEntities.setEvent_time(eventTime);
        loggerEntities.setUser_id(request.getHeader("ga_user_id"));
        loggerEntities.setDevice_id(request.getHeader("ga_username"));
        loggerEntities.setProject_id(loggerProperties.getProjectId());

        long costTime = 0;
        if (request.getHeader("ga_request_id") != null && request.getHeader("ga_request_id").contains("-"))
        {
            long startTime = Long.parseLong(request.getHeader("ga_request_id").split("-")[0]);
            costTime = eventTime - startTime;
        }

        lib.put("lib_language", loggerProperties.getLib().getLanguage());
        lib.put("lib_service_version", loggerProperties.getLib().getServiceVersion());

        properties.put("proj_costTime", String.format("%d", costTime));
        properties.put("proj_projectId", request.getHeader("ga_project_id"));
        properties.put("proj_request_id", request.getHeader("ga_request_id"));
        properties.put("proj_model_name", request.getHeader("ga_model_name"));
        properties.put("proj_service_name", loggerProperties.getProperties().getServiceName());
        properties.put("proj_model_version", loggerProperties.getProperties().getModelVersion());

        loggerEntities.setLib(lib);
        loggerEntities.setProperties(properties);

        return loggerEntities;
    }

    private LoggerEntities initEntitiesSelfRequest(RequestEntities requestEntities, HashMap<String, String> properties, HashMap<String, String> lib)
    {
        long eventTime = System.currentTimeMillis();

        LoggerEntities loggerEntities = new LoggerEntities();
        loggerEntities.setClient_id(loggerProperties.getClientId());
        loggerEntities.setType(TYPE_TRACK);
        loggerEntities.setEvent_time(eventTime);
        loggerEntities.setUser_id(requestEntities.getUserId());
        loggerEntities.setDevice_id(requestEntities.getUserName());
        loggerEntities.setProject_id(loggerProperties.getProjectId());
        loggerEntities.setEvent(requestEntities.getEvent());

        long costTime = 0;

        lib.put("lib_language", loggerProperties.getLib().getLanguage());
        lib.put("lib_service_version", loggerProperties.getLib().getServiceVersion());

        properties.put("proj_costTime", String.format("%d", costTime));
        properties.put("proj_projectId", requestEntities.getProjectId());
        properties.put("proj_request_id", requestEntities.getRequestId());
        properties.put("proj_model_name", requestEntities.getModelName());
        properties.put("proj_service_name", loggerProperties.getProperties().getServiceName());
        properties.put("proj_model_version", loggerProperties.getProperties().getModelVersion());

        loggerEntities.setLib(lib);
        loggerEntities.setProperties(properties);

        return loggerEntities;
    }
}
