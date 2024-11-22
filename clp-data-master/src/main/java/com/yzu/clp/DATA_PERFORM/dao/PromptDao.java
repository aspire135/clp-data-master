package com.yzu.clp.DATA_PERFORM.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.yzu.clp.DATA_PERFORM.dto.DateDTO;
import com.yzu.clp.DATA_PERFORM.entity.Prompt;
import com.yzu.clp.DATA_PERFORM.vo.IndustryTagsCountVO;
import com.yzu.clp.DATA_PERFORM.vo.ProblemTagsCountVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface PromptDao extends BaseMapper<Prompt> {
    List<ProblemTagsCountVO>  getPromblemTagsCount(DateDTO dateDTO);
    List<IndustryTagsCountVO>  getIndustryTagsCount(DateDTO dateDTO);

}
