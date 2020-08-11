package com.tuyoo.framework.grow.common.feign;

import com.tuyoo.framework.grow.common.entities.CommonResult;
import com.tuyoo.framework.grow.common.feign.fallback.GalogFallback;
import com.tuyoo.framework.grow.common.logger.LoggerEntities;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Component
@FeignClient(value = "GROW-ANALYTICS-LOG-SERVER", fallback = GalogFallback.class)
public interface GaLogFeign
{
    @PostMapping("/log/record")
    CommonResult<Object> record(List<LoggerEntities> loggerList);
}
