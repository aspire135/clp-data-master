package com.yzu.clp.DATA_PERFORM.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yzu.clp.DATA_PERFORM.dto.DateDTO;
import com.yzu.clp.DATA_PERFORM.entity.HeiMaoSub;
import com.yzu.clp.DATA_PERFORM.entity.WeeklyData;
import com.yzu.clp.DATA_PERFORM.vo.ClassifyNameVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface HeiMaoSubDao extends BaseMapper<HeiMaoSub> {
    Integer countAll();
    List<ClassifyNameVO> classifyNameByDate(@RequestBody DateDTO dto);

    Integer countAllByDate(LocalDate startDate, LocalDate endDate);

    List<String> getProblem(DateDTO dateDTO);

    List<WeeklyData> getWeeklyData(DateDTO dateDTO);

    Integer countByDate(String startDate, String endDate);

    Integer countByDaylyDate(String currentDayDate);
}
