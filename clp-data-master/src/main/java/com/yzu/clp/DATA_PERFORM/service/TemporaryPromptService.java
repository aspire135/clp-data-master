package com.yzu.clp.DATA_PERFORM.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yzu.clp.DATA_PERFORM.dto.PromptDTO;
import com.yzu.clp.DATA_PERFORM.dto.TemporaryPromptDTO;
import com.yzu.clp.DATA_PERFORM.entity.Prompt;
import com.yzu.clp.DATA_PERFORM.entity.TemporaryPrompt;

import java.awt.*;
import java.io.IOException;
import java.util.Map;

public interface TemporaryPromptService extends IService<TemporaryPrompt> {
    void temporaryPrompt(TemporaryPromptDTO temporaryPromptDTO);

    void createTemporaryTaskBulletin(Map<String, Object> map, Map<String, Object> map1, Map<String, Object> map2, Map<String, Object> map3,
    Map<String, Object> map4) throws IOException, FontFormatException;
}
