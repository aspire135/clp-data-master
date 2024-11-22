package com.yzu.clp.DATA_PERFORM.vo;

import com.yzu.clp.DATA_PERFORM.entity.Prompt;
import com.yzu.clp.DATA_PERFORM.entity.TemporaryPrompt;
import lombok.Data;

import java.util.List;

/**
 * 此vo用来传给算法端参数
 */
@Data
public class PromptTaskVO {

    /**
     * taskid
     */
    private Long taskId;

    private List<TemporaryPrompt> temporaryPromptList;
    private List<Prompt> promptList;
    /**
     * 文件名
     */
    private String fileName;
}
