package com.yzu.clp.DATA_PERFORM.exception;


import com.yzu.clp.DATA_PERFORM.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message("服务异常");
    }

    @ExceptionHandler(ClpException.class)
    @ResponseBody
    public R error(ClpException e){
        e.printStackTrace();
        return R.error().message(e.getMsg()).code(e.getCode());
    }
}
