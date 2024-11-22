package com.yzu.clp.DATA_PERFORM.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yzu.clp.DATA_PERFORM.entity.Prompt;
import com.yzu.clp.DATA_PERFORM.entity.TemporaryPrompt;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface TemporaryPromptDao extends BaseMapper<TemporaryPrompt> {
}
