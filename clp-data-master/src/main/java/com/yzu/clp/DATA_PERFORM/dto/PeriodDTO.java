package com.yzu.clp.DATA_PERFORM.dto;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PeriodDTO {
    @ApiParam("开始时间")
    private LocalDate startDate;
    @ApiParam("结束时间")
    private LocalDate endDate;

    @ApiParam("开始时间-past")
    private LocalDate startDatePast;
    @ApiParam("结束时间-past")
    private LocalDate endDatePast;
}

