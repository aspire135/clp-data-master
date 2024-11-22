package com.yzu.clp.DATA_PERFORM.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@Data
public class R {
    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    @ApiModelProperty(value = "返回码")
    private Integer status;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private Object data ;

    private R(){}

    public static R ok(){
        R r = new R();
        r.setSuccess(true);
        r.setStatus(ResultCode.SUCCESS);
        r.setMessage("成功");
        return r;
    }

    public static R error(){
        R r = new R();
        r.setSuccess(false);
        r.setStatus(ResultCode.ERROR);
        r.setMessage("失败");
        return r;
    }

    public R success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public R message(String message){
        this.setMessage(message);
        return this;
    }

    public R code(Integer status){
        this.setStatus(status);
        return this;
    }

    public R data(Object value){
        this.data = value;
        return this;
    }

    public R data(Map<String, Object> map){
        this.data = map;
        return this;
    }
}
