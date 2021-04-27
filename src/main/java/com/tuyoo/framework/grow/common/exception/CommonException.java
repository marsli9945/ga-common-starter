package com.tuyoo.framework.grow.common.exception;

import com.tuyoo.framework.grow.common.entities.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommonException extends RuntimeException
{
    private Integer code;

    public CommonException(Integer code, String message)
    {
        super(message);
        this.code = code;
    }

    public CommonException(ResultCode resultCode)
    {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    @Override
    public String toString()
    {
        return "CommonException{" + "code=" + code + ", message=" + this.getMessage() + '}';
    }
}
