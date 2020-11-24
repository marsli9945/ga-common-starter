package com.tuyoo.framework.grow.common.result;

import com.tuyoo.framework.grow.common.entities.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@ControllerAdvice
@ConditionalOnWebApplication //web应用生效
@EnableConfigurationProperties(ResultProperties.class)
@ConditionalOnProperty(name = "common.result.enable", havingValue = "true")
public class ResultAdviceAutoConfiguration implements ResponseBodyAdvice<Object>
{
    @Autowired
    ResultProperties resultProperties;

    public ResultAdviceAutoConfiguration()
    {
        super();
        log.info("GA-COMMON =========> ResultAdviceAutoConfiguration action..........");
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass)
    {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse)
    {
        HttpServletResponse response = ((ServletServerHttpResponse) serverHttpResponse).getServletResponse();
        if (resultProperties.getEnableCors())
        {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
            response.setHeader("Access-Control-Allow-Headers", "*");
            response.setHeader("Access-Control-Expose-Headers", "*");
        }

        if (body instanceof CommonResult || resultProperties.getEnableNoPack())
        {
            return body;
        }

        int status = response.getStatus();
        CommonResult<Object> commonResult;
        if (status >= 200 && status < 300)
        {
            commonResult = CommonResult.success(body);
        }
        else
        {
            response.setStatus(200);
            commonResult = CommonResult.init(status, body.toString(), null);
        }

        return commonResult;
    }
}
