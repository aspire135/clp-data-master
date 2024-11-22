package com.yzu.clp.DATA_PERFORM.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class WeeklyData {
    private Integer value;
    private LocalDate date;
}
