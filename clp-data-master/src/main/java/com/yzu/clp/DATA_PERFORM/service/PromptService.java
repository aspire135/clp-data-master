package com.yzu.clp.DATA_PERFORM.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yzu.clp.DATA_PERFORM.dto.PeriodDTO;
import com.yzu.clp.DATA_PERFORM.dto.DateDTO;
import com.yzu.clp.DATA_PERFORM.dto.PromptDTO;
import com.yzu.clp.DATA_PERFORM.entity.Prompt;

import java.util.List;
import java.util.Map;

public interface PromptService extends IService<Prompt> {

    Boolean savePrompt(PromptDTO promptDTO);


    List<Prompt> getAllPrompt();

    Map<String, Integer>  getProblemTagChart(DateDTO dateDTO);

    Map<String, Object> getProblemTagBar(PeriodDTO periodDTO);

    Map<String, Integer> getIndustryTagChart(DateDTO dateDTO);

    Map<String, Object> getIndustryTagBar(PeriodDTO periodDTO);
}
