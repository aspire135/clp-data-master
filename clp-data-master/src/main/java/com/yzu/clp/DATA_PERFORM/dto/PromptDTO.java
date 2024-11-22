package com.yzu.clp.DATA_PERFORM.dto;


import com.yzu.clp.DATA_PERFORM.entity.Prompt;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PromptDTO {

    @ApiModelProperty("分类列表")
    private List<Prompt> promptList;

}
