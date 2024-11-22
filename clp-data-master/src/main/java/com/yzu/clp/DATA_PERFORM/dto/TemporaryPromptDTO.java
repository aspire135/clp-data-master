package com.yzu.clp.DATA_PERFORM.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yzu.clp.DATA_PERFORM.entity.Prompt;
import com.yzu.clp.DATA_PERFORM.entity.TemporaryPrompt;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TemporaryPromptDTO {
    @ApiModelProperty("分类列表")
    private List<TemporaryPrompt> temporaryPromptList;
    private Date startTime;
    private Date endTime;
}
