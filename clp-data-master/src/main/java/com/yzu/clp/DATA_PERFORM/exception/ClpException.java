package com.yzu.clp.DATA_PERFORM.exception;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClpException extends RuntimeException{


    @ApiModelProperty("状态码")
    private Integer code;
    @ApiModelProperty("状态信息")
    private String msg;
    @Override
    public String toString(){
        return "ZrgjException{" +
                "message=" + this.getMessage() +
                ", code=" + code +
                '}';
    }

}
