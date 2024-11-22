package com.yzu.clp.DATA_PERFORM.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yzu.clp.DATA_PERFORM.entity.Task;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.List;

@Data
public class TaskVO {
    private long total;
    private List<Task> tasks;
}
