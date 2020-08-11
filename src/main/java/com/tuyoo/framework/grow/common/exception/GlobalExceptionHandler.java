package com.tuyoo.framework.grow.common.exception;

import com.tuyoo.framework.grow.common.entities.ResultCode;
import com.tuyoo.framework.grow.common.entities.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;


@Slf4j
@ControllerAdvice
@ConditionalOnWebApplication //web应用生效
@EnableConfigurationProperties(ExceptionProperties.class)
@ConditionalOnProperty(name = "common.exception.enable", havingValue = "true")
public class GlobalExceptionHandler
{

    public GlobalExceptionHandler()
    {
        log.info("GA-COMMON =========> GlobalExceptionHandler action......");
    }

    /**
     * -------- 通用异常处理方法 --------
     **/
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonResult<Object> error(Exception e)
    {
        e.printStackTrace();
        return CommonResult.failed(e.getMessage());    // 通用异常结果
    }

    /**
     * -------- 指定异常处理方法 --------
     * 空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public CommonResult<Object> error(NullPointerException e)
    {
        e.printStackTrace();
        return CommonResult.failed(ResultCode.NULL_POINT);
    }

    /**
     * http连接异常
     */
    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseBody
    public CommonResult<Object> error(HttpClientErrorException e)
    {
        e.printStackTrace();
        return CommonResult.failed(ResultCode.HTTP_CLIENT_ERROR);
    }

    /**
     * RequestBody 参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public CommonResult<Object> error(MethodArgumentNotValidException ex)
    {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        assert fieldError != null;
        return CommonResult.validateFailed(fieldError.getField() + fieldError.getDefaultMessage());
    }

    /**
     * query 参数校验
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public CommonResult<Object> error(BindException bindException)
    {
        ArrayList<String> messages = new ArrayList<>();
        for (ObjectError error : bindException.getBindingResult().getAllErrors())
        {
            messages.add(error.getDefaultMessage());
        }
        return CommonResult.validateFailed(messages);
    }


    /**
     * -------- 自定义定异常处理方法 --------
     **/
    @ExceptionHandler(CommonException.class)
    @ResponseBody
    public CommonResult<Object> error(CommonException e)
    {
        e.printStackTrace();
        return CommonResult.init(e.getCode(), e.getMessage(), null);
    }
}
