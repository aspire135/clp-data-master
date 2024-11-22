package com.yzu.clp.DATA_PERFORM.dto;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DateDTO {
    @ApiParam("开始时间")
    private LocalDate startDate;
    @ApiParam("结束时间")
    private LocalDate endDate;
}
