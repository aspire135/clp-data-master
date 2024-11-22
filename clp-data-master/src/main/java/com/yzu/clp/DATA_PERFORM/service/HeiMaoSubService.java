package com.yzu.clp.DATA_PERFORM.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yzu.clp.DATA_PERFORM.dto.PeriodDTO;
import com.yzu.clp.DATA_PERFORM.dto.DateDTO;
import com.yzu.clp.DATA_PERFORM.entity.HeiMaoSub;
import com.yzu.clp.DATA_PERFORM.entity.WeeklyData;
import com.yzu.clp.DATA_PERFORM.vo.ClassifyNameVO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface HeiMaoSubService extends IService<HeiMaoSub> {
    Map<String,Integer> countAll();

    List<ClassifyNameVO> classifyNameByDate(PeriodDTO periodDTO);


    void getFileListRecursive(String filePath, List<String> fileList);

    Integer countAllByDate(LocalDate startDate, LocalDate endDate);

    Map<String, Object> classifyNameToBarByDate(PeriodDTO periodDTO);

    /**
     * name value
     * @return
     */
    List<ClassifyNameVO> getWordle();

    List<WeeklyData> getWeeklyData(DateDTO dateDTO);
}
