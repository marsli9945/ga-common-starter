package com.tuyoo.framework.grow.common.feign.fallback;

import com.tuyoo.framework.grow.common.entities.CommonResult;
import com.tuyoo.framework.grow.common.feign.GaLogFeign;
import com.tuyoo.framework.grow.common.logger.LoggerEntities;

import java.util.List;

public class GalogFallback implements GaLogFeign
{
    @Override
    public CommonResult<Object> record(List<LoggerEntities> loggerList)
    {
        return CommonResult.failed("请求超时");
    }
}
