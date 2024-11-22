package com.yzu.clp.DATA_PERFORM.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BasePage {

    @ApiModelProperty(value = "页码", required = true)
    private long current ;

    /**
     * 默认20条
     */
    @ApiModelProperty(value = "每页显示多少条", required = true)
    private long size ;

}
